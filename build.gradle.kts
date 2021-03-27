import java.io.OutputStream

plugins {
    kotlin("jvm") version "1.4.31"
    kotlin("plugin.serialization") version "1.4.31"
    id("com.github.johnrengelman.shadow") version "5.2.0"
//    `maven-publish`
}

val relocate = (findProperty("relocate") as? String)?.toBoolean() ?: true

repositories {
    mavenLocal()
    mavenCentral()
    maven(url = "https://papermc.io/repo/repository/maven-public/")
    maven(url = "https://jitpack.io/")
}

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")

    implementation("com.github.monun:tap:+")
    implementation("com.github.monun:kommand:+")

//    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
//    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.0")
//    testImplementation("org.mockito:mockito-core:3.6.28")
//    testImplementation("org.spigotmc:spigot:1.16.5-R0.1-SNAPSHOT")
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }
    processResources {
        filesMatching("**/*.yml") {
            expand(project.properties)
        }
    }
    test {
        useJUnitPlatform()
        doLast {
            file("logs").deleteRecursively()
        }
    }
    create<Jar>("sourcesJar") {
        from(sourceSets["main"].allSource)
        archiveClassifier.set("sources")
    }
    shadowJar {
        archiveBaseName.set(project.property("pluginName").toString())
        archiveVersion.set("") // For bukkit plugin update
        archiveClassifier.set("") // Remove 'all'

        if (relocate) {
            relocate("com.github.monun.kommand", "${rootProject.group}.${rootProject.name}.kommand")
            relocate("com.github.monun.tap", "${rootProject.group}.${rootProject.name}.tap")
        }

        doFirst {
            println("relocate = $relocate")
        }
    }
    build {
        dependsOn(shadowJar)
    }
    create<Copy>("copyToServer") {
        from(shadowJar)
        val plugins = File(rootDir, ".server/plugins")
        if (File(plugins, shadowJar.get().archiveFileName.get()).exists()) {
            into(File(plugins, "update"))
        } else {
            into(plugins)
        }
    }
    create<DefaultTask>("setupWorkspace") {
        doLast {
            val versions = arrayOf(
                "1.16.5"
            )
            val buildtoolsDir = file(".buildtools")
            val buildtools = File(buildtoolsDir, "BuildTools.jar")

            val maven = File(System.getProperty("user.home"), ".m2/repository/org/spigotmc/spigot/")
            val repos = maven.listFiles { file: File -> file.isDirectory } ?: emptyArray()
            val missingVersions = versions.filter { version ->
                repos.find { it.name.startsWith(version) }?.also { println("Skip downloading spigot-$version") } == null
            }.also { if (it.isEmpty()) return@doLast }

            val download by registering(de.undercouch.gradle.tasks.download.Download::class) {
                src("https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar")
                dest(buildtools)
            }
            download.get().download()

            runCatching {
                for (v in missingVersions) {
                    println("Downloading spigot-$v...")

                    javaexec {
                        workingDir(buildtoolsDir)
                        main = "-jar"
                        args = listOf("./${buildtools.name}", "--rev", v)
                        // Silent
                        standardOutput = OutputStream.nullOutputStream()
                        errorOutput = OutputStream.nullOutputStream()
                    }
                }
            }.onFailure {
                it.printStackTrace()
            }
            buildtoolsDir.deleteRecursively()
        }
    }
}

//publishing {
//    publications {
//        create<MavenPublication>(project.property("pluginName").toString()) {
//            artifactId = project.name
//            from(components["java"])
//            artifact(tasks["sourcesJar"])
//        }
//    }
//}
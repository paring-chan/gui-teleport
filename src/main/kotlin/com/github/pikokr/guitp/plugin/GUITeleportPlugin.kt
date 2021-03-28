package com.github.pikokr.guitp.plugin

import com.github.monun.kommand.kommand
import com.github.pikokr.guitp.command.TeleportGUICommand
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author pikokr
 */
class GUITeleportPlugin : JavaPlugin() {
    companion object {
        lateinit var instance: GUITeleportPlugin
    }

    override fun onEnable() {
        instance = this
        kommand {
            register("tpgui") {
                TeleportGUICommand.register(this)
            }
        }
        server.pluginManager.registerEvents(GUITeleportListener(), this)
    }
}
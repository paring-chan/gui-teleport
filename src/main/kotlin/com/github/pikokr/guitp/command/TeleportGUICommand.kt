package com.github.pikokr.guitp.command

import com.github.monun.kommand.KommandBuilder
import com.github.monun.kommand.argument.player
import com.github.pikokr.guitp.plugin.GUITeleportPlugin
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object TeleportGUICommand {
    fun register(builder: KommandBuilder) {
        builder.apply {
            then("give") {
                then("player" to player()) {
                    executes {
                        val p = it.parseArgument<Player>("player")
                        p.inventory.addItem(ItemStack(Material.KNOWLEDGE_BOOK).apply {
                            itemMeta = itemMeta.apply {
                                persistentDataContainer.set(NamespacedKey(GUITeleportPlugin.instance, "type"), PersistentDataType.STRING, "TP_MENU")
                                displayName(Component.text("티피권"))
                            }
                        })
                    }
                }
            }
        }
    }
}
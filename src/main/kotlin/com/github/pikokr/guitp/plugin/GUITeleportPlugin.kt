package com.github.pikokr.guitp.plugin

import com.github.monun.kommand.kommand
import com.github.pikokr.guitp.command.TeleportGUICommand
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author pikokr
 */
class GUITeleportPlugin : JavaPlugin() {
    override fun onEnable() {
        kommand {
            register("tpgui") {
                TeleportGUICommand.register(this)
            }
        }
    }
}
package com.github.pikokr.guitp.command

import com.github.monun.invfx.openWindow
import com.github.monun.kommand.KommandBuilder
import com.github.pikokr.guitp.invfx.TeleportInv
import org.bukkit.entity.Player

object TeleportGUICommand {
    fun register(builder: KommandBuilder) {
        builder.apply {
            executes {
                val p = it.sender as Player
                p.openWindow(TeleportInv.create(p))
            }
        }
    }
}
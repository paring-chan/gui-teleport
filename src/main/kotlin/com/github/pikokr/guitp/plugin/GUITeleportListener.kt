package com.github.pikokr.guitp.plugin

import com.github.monun.invfx.openWindow
import com.github.pikokr.guitp.invfx.TeleportInv
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataType

class GUITeleportListener : Listener {
    @EventHandler
    fun onRightClickItem(event: PlayerInteractEvent) {
        event.item?.also {
            val meta = it.itemMeta
            if (meta.persistentDataContainer.get(NamespacedKey(GUITeleportPlugin.instance, "type"), PersistentDataType.STRING) != "TP_MENU") return
        } ?: return
        event.isCancelled = true
        event.player.openWindow(TeleportInv.create(event.player))
    }
}
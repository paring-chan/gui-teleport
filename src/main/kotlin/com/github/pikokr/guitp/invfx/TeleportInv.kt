package com.github.pikokr.guitp.invfx

import com.github.monun.invfx.InvFX
import com.github.monun.invfx.InvScene
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

object TeleportInv {
    private val previousItem =
        ItemStack(Material.END_CRYSTAL).apply { itemMeta = itemMeta.apply { displayName(Component.text("${ChatColor.RESET}${ChatColor.BOLD}←")) } }
    private val nextItem =
        ItemStack(Material.END_CRYSTAL).apply { itemMeta = itemMeta.apply { displayName(Component.text("${ChatColor.RESET}${ChatColor.BOLD}→")) } }

    fun create(player: Player): InvScene {
        return InvFX.scene(1, "${ChatColor.DARK_BLUE}TELEPORT") {
            panel(0, 0, 9, 1) {
                listView(2, 0, 5, 1, true, listOf(player, player, player)) {
                    transform { ItemStack(Material.PLAYER_HEAD).apply {
                        itemMeta = (itemMeta as SkullMeta).apply {
                            owningPlayer = it
                            displayName(Component.text(player.name))
                        }
                    } }
                }.let { view ->
                    button(1, 0) {
                        onInit {
                            it.item = previousItem
                        }
                        onClick { _ , _ ->
                            view.index--
                        }
                    }
                    button(8, 0) {
                        onInit {
                            it.item = nextItem
                        }
                        onClick { _ , _ ->
                            view.index++
                        }
                    }
                }
            }.let { panel ->
                panel.setItem(0, 0, ItemStack(Material.BOOK).apply {
                    itemMeta = itemMeta.apply {
                        displayName(Component.text("도움말"))
                        lore(listOf(Component.text("플레이어를 선택해 이동하세요"), Component.text("엔드 크리스탈을 클릭해 스크롤 가능합니다.")))
                    }
                })
            }
        }
    }
}
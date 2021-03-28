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
        ItemStack(Material.END_CRYSTAL).apply {
            itemMeta = itemMeta.apply { displayName(Component.text("${ChatColor.RESET}${ChatColor.BOLD}←")) }
        }
    private val nextItem =
        ItemStack(Material.END_CRYSTAL).apply {
            itemMeta = itemMeta.apply { displayName(Component.text("${ChatColor.RESET}${ChatColor.BOLD}→")) }
        }

    private val guideBook = ItemStack(Material.BOOK).apply {
        itemMeta = itemMeta.apply {
            displayName(Component.text("설명서"))
            lore(
                listOf(
                    Component.text(
                        ChatColor.translateAlternateColorCodes(
                            '&',
                            "&f&l플레이어의 머리를 클릭할 시 플레이어에게 티피 합니다."
                        )
                    )
                )
            )
        }
    }

    val firstIgnoreList = arrayOf(4)
    val lastIgnoreList = arrayOf(3, 4, 5)

    private val glass = ItemStack(Material.BLUE_STAINED_GLASS_PANE).apply {
        itemMeta = itemMeta.apply {
            displayName(Component.text(""))
        }
    }

    fun create(player: Player): InvScene {
        return InvFX.scene(3, "${ChatColor.DARK_BLUE}TELEPORT") {
            panel(0, 0, 9, 3) {
                listView(1, 1, 7, 1, true, Bukkit.getOnlinePlayers().toList()) {
                    transform {
                        ItemStack(Material.PLAYER_HEAD).apply {
                            itemMeta = (itemMeta as SkullMeta).apply {
                                owningPlayer = it
                                displayName(Component.text(it.name))
                                if (it === player) {
                                    lore(listOf(Component.text("${ChatColor.RED}자신에게 TP할 수 없습니다.")))
                                }
                            }
                        }
                    }
                    onClickItem { _, _, _, item, _ ->
                        player.teleport(item.location)
                    }
                }.let { view ->
                    button(3, 2) {
                        onInit {
                            it.item = previousItem
                        }
                        onClick { _, _ ->
                            view.index--
                        }
                    }
                    button(5, 2) {
                        onInit {
                            it.item = nextItem
                        }
                        onClick { _, _ ->
                            view.index++
                        }
                    }
                }
            }
                .let { panel ->
                    panel.setItem(4, 2, guideBook)
                    for (i in 0..8) {
                        panel.setItem(i, 0, glass)
                    }
                    panel.setItem(0, 1, glass)
                    panel.setItem(8, 1, glass)
                    for (i in 0..8) {
                        if (lastIgnoreList.contains(i)) continue
                        panel.setItem(i, 2, glass)
                    }
                }
        }
    }
}
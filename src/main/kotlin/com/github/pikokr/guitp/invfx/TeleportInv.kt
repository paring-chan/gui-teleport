package com.github.pikokr.guitp.invfx

import com.github.monun.invfx.InvFX
import com.github.monun.invfx.InvScene
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object TeleportInv {
    fun create(player: Player): InvScene {
        return InvFX.scene(1, "${ChatColor.DARK_BLUE}TELEPORT") {
            panel(0, 0, 9, 1) {
                onInit {
                    it.setItem(0, 0, ItemStack(Material.BOOK).apply {
                        itemMeta = itemMeta.apply {
                            displayName(Component.text("도움말"))
                            lore(listOf(Component.text("플레이어를 선택해 이동하세요"), Component.text("엔드 크리스탈을 클릭해 스크롤 가능합니다.")))
                        }
                    })
                }
            }
        }
    }
}
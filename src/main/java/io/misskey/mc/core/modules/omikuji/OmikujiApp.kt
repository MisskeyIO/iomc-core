package io.misskey.mc.core.modules.omikuji

import org.bukkit.Material
import org.bukkit.entity.Player
import io.misskey.mc.core.modules.gameMenu.AppBase

/**
 * おみくじアプリ
 * @author Lutica
 */
class OmikujiApp : AppBase() {
    override fun getName(player: Player): String = "おみくじ"

    override fun getIcon(player: Player): Material = Material.GOLD_INGOT

    override fun onLaunch(player: Player) {
        player.performCommand("omikuji")
    }
}

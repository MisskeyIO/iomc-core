package io.misskey.mc.core.modules.cat

import org.bukkit.Material
import org.bukkit.entity.Player
import io.misskey.mc.core.modules.cat.CatModule.isCat
import io.misskey.mc.core.modules.cat.CatModule.setCat
import io.misskey.mc.core.modules.gameMenu.AppBase

/**
 * ネコモードを切り替えるプラグイン。
 * @author Lutica
 */
class CatApp : AppBase() {
    override fun getName(player: Player): String {
        return "ネコ語モードを${if (isCat(player)) "オフ" else "オン"}にする"
    }

    override fun getIcon(player: Player): Material = Material.COD

    override fun onLaunch(player: Player) {
        setCat(player, !isCat(player))
    }

    override fun isShiny(player: Player): Boolean = isCat(player)
}

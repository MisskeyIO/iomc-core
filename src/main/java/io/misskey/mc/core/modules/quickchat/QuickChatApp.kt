package io.misskey.mc.core.modules.quickchat

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import io.misskey.mc.core.gui.Gui
import io.misskey.mc.core.gui.MenuItem
import io.misskey.mc.core.gui.SoundPitch
import io.misskey.mc.core.modules.gameMenu.AppBase

/**
 * クイックチャットアプリ
 * @author raink1208
 */
class QuickChatApp : AppBase() {
    override fun getName(player: Player): String = "クイックチャット"

    override fun getIcon(player: Player): Material = Material.PAPER

    override fun onLaunch(player: Player) {
        val list = ArrayList<MenuItem>()
        val ui = Gui.getInstance()
        ui.playSound(player, Sound.ENTITY_VILLAGER_WORK_CARTOGRAPHER, 1f, SoundPitch.F_1)

        for (chat in QuickChatModule.getAllPrefix()) {
            val msg = QuickChatModule.chatFormat(QuickChatModule.getMessage(chat), player)
            list.add(
                MenuItem(String.format("%s ${ChatColor.GRAY}(.%s)", msg, chat), {
                    player.chat(msg)
                }, Material.PAPER)
            )
        }
        ui.openMenu(player, "クイックチャット", list)
    }
}
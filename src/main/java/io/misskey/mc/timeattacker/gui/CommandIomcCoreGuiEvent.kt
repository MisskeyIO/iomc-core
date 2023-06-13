package io.misskey.mc.timeattacker.gui

import org.bukkit.command.Command
import org.bukkit.entity.Player
import io.misskey.mc.timeattacker.api.commands.CommandPlayerOnlyBase
import io.misskey.mc.timeattacker.gui.Gui.Companion.getInstance

/**
 * Java版で看板を使用したダイアログのボタンを
 * クリックしたときに内部的に送られる隠しコマンド
 * プレイヤーが使用することは想定していない
 * @author Lutica
 */
class CommandIomcCoreGuiEvent : CommandPlayerOnlyBase() {
    override fun execute(player: Player, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.size != 1) return false
        getInstance().handleCommand(args[0])
        return true
    }
}
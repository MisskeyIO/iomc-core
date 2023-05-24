package io.misskey.mc.core.modules.gameMenu

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import io.misskey.mc.core.api.commands.CommandPlayerOnlyBase

/**
 * ゲームメニューを表示するコマンド
 * @author Lutica
 */
class MenuCommand : CommandPlayerOnlyBase() {
    private val subCommands = listOf("get")
    override fun execute(player: Player, command: Command, label: String, args: Array<out String>): Boolean {
        GameMenuModule.open(player)
        return true
    }

    override fun onTabComplete(commandSender: CommandSender, command: Command, label: String, args: Array<String>): List<String> {
        return if (args.size > 1) COMPLETE_LIST_EMPTY else subCommands
    }
}
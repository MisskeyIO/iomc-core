package io.misskey.mc.core.api.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import io.misskey.mc.core.modules.mobball.MobBallModule
import io.misskey.mc.core.modules.notification.NotificationModule

/**
 * iomcCore の設定ファイルなどの再読み込みコマンド。
 */
class CommandXReload : CommandBase() {
    override fun execute(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) return false
        if (args[0] == "all" || args[0] == "mobball") MobBallModule.reload()
        if (args[0] == "all" || args[0] == "notification") NotificationModule.reload()
        return true
    }

    override fun onTabComplete(commandSender: CommandSender, command: Command, label: String, args: Array<String>): MutableList<String> {
        return if (args.size == 1) mutableListOf("all", "mobball", "notification") else mutableListOf()
    }
}
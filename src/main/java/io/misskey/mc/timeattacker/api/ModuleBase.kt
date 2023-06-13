package io.misskey.mc.timeattacker.api

import org.bukkit.Bukkit
import org.bukkit.event.Listener
import io.misskey.mc.timeattacker.TimeattackerPlugin
import io.misskey.mc.timeattacker.api.commands.CommandBase
import io.misskey.mc.timeattacker.api.commands.CommandRegistry

/**
 * モジュールの基底クラス。
 */
abstract class ModuleBase {
    open fun onEnable() {}
    open fun onPostEnable() {}
    open fun onDisable() {}

    protected fun registerCommand(name: String, command: CommandBase) {
        CommandRegistry.register(name, command)
    }

    protected fun registerHandler(handler: Listener) {
        Bukkit.getPluginManager().registerEvents(handler, TimeattackerPlugin.instance)
    }
}
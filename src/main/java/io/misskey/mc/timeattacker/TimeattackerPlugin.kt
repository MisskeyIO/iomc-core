package io.misskey.mc.timeattacker

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import io.misskey.mc.timeattacker.api.ModuleBase
import io.misskey.mc.timeattacker.api.commands.CommandRegistry
import io.misskey.mc.timeattacker.api.commands.CommandXDebug
import io.misskey.mc.timeattacker.api.playerStore.PlayerStore
import io.misskey.mc.timeattacker.gui.Gui
import io.misskey.mc.timeattacker.modules.counter.CounterModule
import io.misskey.mc.timeattacker.modules.ranking.RankingModule

/**
 * iomcCore のメインクラスであり、構成する要素を初期化・管理しています。
 * @author Lutica
 */
class TimeattackerPlugin : JavaPlugin() {

    override fun onEnable() {
        logger.info("${ChatColor.AQUA}------------------------------------------")
        logger.info("  ${ChatColor.GREEN}Timeattacker${ChatColor.GRAY} ver ${description.version}")
        logger.info("${ChatColor.AQUA}------------------------------------------")
        instance = this
        initializeFoundation()

        loadModules()
    }

    override fun onDisable() {
        CommandRegistry.clearMap()
        PlayerStore.onDisable()
        unloadModules()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        return CommandRegistry.onCommand(sender, command, label, args)
    }

    /**
     * モジュールを全て有効化します。
     */
    private fun loadModules() {
        logger.info("モジュール 総数 ${modules.size}")
        // モジュールの有効化フック
        modules.forEach {
            try {
                it.onEnable()
                logger.info("${ChatColor.GRAY}- ${it.javaClass.simpleName}${ChatColor.GREEN} » ○")
            } catch (e: Exception) {
                logger.severe("${ChatColor.GRAY}- ${it.javaClass.simpleName}${ChatColor.RED} » ×")
                e.printStackTrace()
            }
        }
        // モジュールの有効化後処理フック（各モジュールの連携とか）
        modules.forEach {
            try {
                it.onPostEnable()
            } catch (e: Exception) {
                logger.severe("モジュール '${it.javaClass.simpleName}' の初期化後処理の実行に失敗しました。")
                e.printStackTrace()
            }
        }
        logger.info("モジュールを全て読み込み、有効化しました。")
    }

    /**
     * モジュールを全て無効化します。
     */
    private fun unloadModules() {
        // モジュールの無効化フック
        modules.forEach {
            try {
                it.onDisable()
            } catch (e: Exception) {
                logger.severe("モジュール '${it.javaClass.name}' の無効化に失敗しました。")
                e.printStackTrace()
            }
        }
        logger.info("モジュールを全て無効化しました。")
    }

    private fun initializeFoundation() {
        PlayerStore.onEnable()
        Gui.onEnable()
        CommandRegistry.register("xdebug", CommandXDebug())

        Bukkit.getOnlinePlayers().forEach { it.updateCommands() }
    }

    private val modules: Array<ModuleBase> = arrayOf(
        CounterModule,
        RankingModule,
    )

    companion object {
        lateinit var instance: TimeattackerPlugin private set
    }
}
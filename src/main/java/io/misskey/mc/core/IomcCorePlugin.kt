package io.misskey.mc.core

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import io.misskey.mc.core.api.HookBase
import io.misskey.mc.core.api.ModuleBase
import io.misskey.mc.core.api.commands.CommandRegistry
import io.misskey.mc.core.api.commands.CommandXDebug
import io.misskey.mc.core.api.commands.CommandXReload
import io.misskey.mc.core.api.playerStore.PlayerStore
import io.misskey.mc.core.gui.Gui
import io.misskey.mc.core.hooks.CitizensHook
import io.misskey.mc.core.hooks.VaultHook
import io.misskey.mc.core.modules.autoCrafter.AutoCrafterModule
import io.misskey.mc.core.modules.bossbar.BossBarModule
import io.misskey.mc.core.modules.cat.CatModule
import io.misskey.mc.core.modules.countdown.CountdownModule
import io.misskey.mc.core.modules.counter.CounterModule
import io.misskey.mc.core.modules.fly.FlyModule
import io.misskey.mc.core.modules.grenade.GrenadeModule
import io.misskey.mc.core.modules.item.ItemModule
import io.misskey.mc.core.modules.kusa.KusaModule
import io.misskey.mc.core.modules.livemode.LiveModeModule
import io.misskey.mc.core.modules.mobball.MobBallModule
import io.misskey.mc.core.modules.nbs.NbsModule
import io.misskey.mc.core.modules.notification.NotificationModule
import io.misskey.mc.core.modules.omikuji.OmikujiModule
import io.misskey.mc.core.modules.quickchat.QuickChatModule
import io.misskey.mc.core.modules.ranking.RankingModule
import io.misskey.mc.core.modules.stamprally.StampRallyModule
import io.misskey.mc.core.modules.gameMenu.GameMenuModule
import io.misskey.mc.core.utils.Ticks

/**
 * iomcCore のメインクラスであり、構成する要素を初期化・管理しています。
 * @author Lutica
 */
class IomcCorePlugin : JavaPlugin() {

    override fun onEnable() {
        logger.info("${ChatColor.AQUA}------------------------------------------")
        logger.info("  ${ChatColor.GREEN}iomcCore${ChatColor.GRAY} - Misskey.io Minecraft Core System Plugin")
        logger.info("      ${ChatColor.GRAY}ver ${description.version}")
        logger.info("${ChatColor.AQUA}------------------------------------------")
        instance = this
        initializeFoundation()

        loadHooks()
        loadModules()
    }

    override fun onDisable() {
        CommandRegistry.clearMap()
        PlayerStore.onDisable()
        unloadHooks()
        unloadModules()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        return CommandRegistry.onCommand(sender, command, label, args)
    }

    /**
     * 連携フックを全て有効化します。
     */
    private fun loadHooks() {
        logger.info("連携フック 総数 ${hooks.size}")
        // 連携フックの有効化
        hooks.forEach {
            try {
                it.onEnable()
                logger.info("${ChatColor.GRAY}- ${it.javaClass.simpleName}${ChatColor.GREEN} » ○")
            } catch (e: Exception) {
                logger.severe("${ChatColor.GRAY}- ${it.javaClass.simpleName}${ChatColor.RED} » ×")
                e.printStackTrace()
            }
        }
        logger.info("連携フックを全て読み込み、有効化しました。")
    }

    /**
     * 連携フックを全て無効化します。
     */
    private fun unloadHooks() {
        // 連携フックの有効化
        hooks.forEach {
            try {
                it.onDisable()
            } catch (e: Exception) {
                logger.severe("連携フック '${it.javaClass.simpleName}' の無効化に失敗しました。")
                e.printStackTrace()
            }
        }
        logger.info("連携フックを全て無効化しました。")
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
        CommandRegistry.register("xreload", CommandXReload())
        CommandRegistry.register("xdebug", CommandXDebug())
        TimeObserver().runTaskTimer(this, 0, Ticks.from(1.0).toLong())

        Bukkit.getOnlinePlayers().forEach { it.updateCommands() }
    }

    private val hooks: Array<HookBase> = arrayOf(
        CitizensHook,
        VaultHook,
    )

    private val modules: Array<ModuleBase> = arrayOf(
        AutoCrafterModule,
        BossBarModule,
        CatModule,
        CountdownModule,
        CounterModule,
        FlyModule,
        GameMenuModule,
        GrenadeModule,
        ItemModule,
        KusaModule,
        LiveModeModule,
        MobBallModule,
        NbsModule,
        NotificationModule,
        OmikujiModule,
        QuickChatModule,
        RankingModule,
        StampRallyModule,
    )

    companion object {
        lateinit var instance: IomcCorePlugin private set
    }
}
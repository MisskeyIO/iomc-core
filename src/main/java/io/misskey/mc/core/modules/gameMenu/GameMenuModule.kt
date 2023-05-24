package io.misskey.mc.core.modules.gameMenu

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.entity.Player
import io.misskey.mc.core.api.ModuleBase
import io.misskey.mc.core.gui.Gui
import io.misskey.mc.core.gui.MenuItem
import io.misskey.mc.core.gui.SoundPitch
import io.misskey.mc.core.modules.cat.CatApp
import io.misskey.mc.core.modules.livemode.LiveModeApp
import io.misskey.mc.core.modules.notification.NotificationApp
import io.misskey.mc.core.modules.omikuji.OmikujiApp
import io.misskey.mc.core.modules.quickchat.QuickChatApp
import io.misskey.mc.core.modules.stamprally.StampRallyApp

/**
 * ゲームメニュー機能を提供します。
 */
object GameMenuModule : ModuleBase() {
    private val MENU_TITLE = "${ChatColor.GREEN}${ChatColor.ITALIC}ゲームメニュー"
    private lateinit var apps: MutableList<AppBase>

    /**
     * iomcCore が有効になったときに呼ばれます。
     */
    override fun onEnable() {
        apps = mutableListOf(
            NotificationApp(),
            OmikujiApp(),
            CatApp(),
            LiveModeApp(),
            QuickChatApp(),
            StampRallyApp(),
        )

        registerCommand("menu", MenuCommand())
        registerHandler(GameMenuHandler())
    }

    /**
     * iomcCore が無効になったときに呼ばれます。
     */
    override fun onDisable() {
        apps.clear()
    }

    /**
     * X Phone にアプリを登録します。
     */
    fun registerApp(app: AppBase) {
        if (apps.contains(app)) {
            Bukkit.getLogger().warning("X Phoneアプリ「${app.javaClass.typeName}」は既に登録されているため、無視します。")
            return
        }
        apps.add(app)
        Bukkit.getLogger().warning("X Phoneアプリ「${app.javaClass.typeName}」を登録しました。")
    }

    /**
     * X Phone のメニュー画面を呼び出します。
     */
    fun open(player: Player) {
        playStartupSound(player)
        ui().openMenu(player, MENU_TITLE, apps.filter {
            it.isVisible(player)
        }.map { app ->
            MenuItem(app.getName(player), { app.onLaunch(player) }, app.getIcon(player), null, app.isShiny(player))
        })
    }

    fun ui() = Gui.getInstance()

    /**
     * 起動音を再生します。
     */
    fun playStartupSound(player: Player) {
        ui().playSound(player, Sound.BLOCK_NOTE_BLOCK_HARP, 1f, SoundPitch.A1)
        ui().playSoundAfter(player, Sound.BLOCK_NOTE_BLOCK_HARP, 1f, SoundPitch.D2, 4)
        ui().playSoundAfter(player, Sound.BLOCK_NOTE_BLOCK_HARP, 1f, SoundPitch.C_2, 8)
    }

    /**
     * 通知音を再生します。
     */
    fun playTritone(player: Player) {
        ui().playSoundLocally(player, Sound.BLOCK_NOTE_BLOCK_HARP, 1f, SoundPitch.D1)
        ui().playSoundLocallyAfter(player, Sound.BLOCK_NOTE_BLOCK_HARP, 1f, SoundPitch.A1, 3)
        ui().playSoundLocallyAfter(player, Sound.BLOCK_NOTE_BLOCK_HARP, 1f, SoundPitch.D2, 6)
    }
}
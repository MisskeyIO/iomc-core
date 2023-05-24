package io.misskey.mc.core.modules.cat

import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.Sound
import org.bukkit.entity.Player
import io.misskey.mc.core.api.ModuleBase
import io.misskey.mc.core.api.playerStore.PlayerStore
import io.misskey.mc.core.gui.Gui
import io.misskey.mc.core.gui.SoundPitch

/**
 * チャットのテキストをネコ語に置き換える、ネコモードを提供するモジュールです。
 */
object CatModule : ModuleBase() {
    const val PS_KEY_CAT = "cat"

    override fun onEnable() {
        registerHandler(CatHandler())
        registerCommand("cat", CommandCat())
    }

    /**
     * [text] をネコ語に変換します。
     */
    fun nyaize(text: String): String {
        return text
            .replace("な", "にゃ")
            .replace("ナ", "ニャ")
            .replace("ﾅ", "ﾆｬ")
            .replace("everyone", "everynyan")
            .replace("morning", "mornyan")
            .replace("na", "nya")
            .replace("EVERYONE", "EVERYNYAN")
            .replace("MORNING", "MORNYAN")
            .replace("NA", "NYA")
    }

    /**
     * [player] がネコモードを true にしているかどうかを取得します。
     */
    fun isCat(player: OfflinePlayer): Boolean {
        return PlayerStore.open(player).getBoolean(PS_KEY_CAT)
    }

    /**
     * [player] のネコモードを [value] に設定します。
     */
    fun setCat(player: Player, value: Boolean) {
        PlayerStore.open(player)[PS_KEY_CAT] = value
        Gui.getInstance().playSound(player, Sound.ENTITY_CAT_AMBIENT, 2f, if (value) SoundPitch.F_2 else SoundPitch.F_0)
        player.sendMessage("CATモードを${if (value) "${ChatColor.GREEN}オン" else "${ChatColor.RED}オフ"}${ChatColor.RESET}にしました。")
    }
}


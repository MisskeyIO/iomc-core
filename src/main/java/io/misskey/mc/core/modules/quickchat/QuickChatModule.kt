package io.misskey.mc.core.modules.quickchat

import org.bukkit.entity.Player
import io.misskey.mc.core.api.Config
import io.misskey.mc.core.api.ModuleBase
import java.io.IOException

/**
 * クイックチャット機能を提供するモジュールです。
 * @author raink1208
 */
object QuickChatModule : ModuleBase() {
    lateinit var config: Config

    override fun onEnable() {
        config = Config("quickChats")
        registerCommand("qchat", QuickChatCommand())
        registerHandler(QuickChatHandler())
    }

    /**
     * 全てのクイックチャット プレフィックスを取得します。
     */
    fun getAllPrefix(): Set<String> {
        return config.conf.getKeys(false)
    }

    /**
     * [prefix] に対応するメッセージを取得します。
     */
    fun getMessage(prefix: String): String {
        return config.conf.getString(prefix) ?: prefix
    }

    /**
     * [msg] に埋め込まれた変数を展開します。
     */
    fun chatFormat(msg: String, player: Player): String {
        var text = msg

        text = text.replace("{world}", player.world.name)
        text = text.replace("{x}", player.location.blockX.toString())
        text = text.replace("{y}", player.location.blockY.toString())
        text = text.replace("{z}", player.location.blockZ.toString())

        return text
    }

    /**
     * プレフィックス [prefix] を [msg] という内容で登録します。
     */
    fun register(prefix: String, msg: String): Boolean {
        if (config.conf.contains(prefix)) return false

        config.conf.set(prefix, msg)
        try {
            config.save()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return true
    }

    /**
     * プレフィックス [prefix] の登録を解除します。
     */
    fun unregister(prefix: String): Boolean {
        if (!config.conf.contains(prefix)) return false
        config.conf.set(prefix, null)
        try {
            config.save()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return true
    }
}
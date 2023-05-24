package io.misskey.mc.core.modules.counter

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import io.misskey.mc.core.api.playerStore.PlayerStore
import io.misskey.mc.core.utils.Time

/**
 * タイムアタック走者に時間を出すプログラム
 */
class TimeAttackObserver : BukkitRunnable() {
    override fun run() {
        // TODO パフォーマンス改善のため、走者をイベントハンドラーでリスト化し、キャッシュするようにする
        Bukkit.getOnlinePlayers().forEach {
            val record = PlayerStore.open(it)
            if (!record.has(CounterModule.PS_KEY_ID)) return@forEach

            val ts = record.getString(CounterModule.PS_KEY_TIME, "0").toLong()
            val now = System.currentTimeMillis()
            val timeString = Time.msToString(now - ts)
            it.sendActionBar(Component.text(timeString))
        }
    }
}
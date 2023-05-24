package io.misskey.mc.core

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import io.misskey.mc.core.api.events.NewMorningEvent
import io.misskey.mc.core.api.events.RealTimeNewDayEvent
import java.time.LocalDateTime

/**
 * 時間イベントのための監視 Runnable。
 * 監視する時間には、サーバーOSのタイムゾーンを使用しています。
 * @author Lutica
 */
class TimeObserver : BukkitRunnable() {
    override fun run() {
        val pm = Bukkit.getPluginManager()
        val currentGameTime = Bukkit.getWorld("main")!!.time
        val currentRealTime = LocalDateTime.now()

        // 時間が前よりも小さくなったのであれば、おそらく日をまたいだことになる
        if (currentGameTime < previousGameTime) {
            val event = NewMorningEvent(currentGameTime)
            pm.callEvent(event)
        }

        // 時刻が4時になったら RealTimeNewDayEvent を発行
        if (currentRealTime.hour == 4 && previousRealTime.hour != 4) {
            pm.callEvent(RealTimeNewDayEvent())
        }

        previousGameTime = currentGameTime
        previousRealTime = currentRealTime
    }

    private var previousGameTime: Long = 0
    private var previousRealTime: LocalDateTime = LocalDateTime.now()
}
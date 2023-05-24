package io.misskey.mc.core.modules.notification

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.scheduler.BukkitRunnable
import io.misskey.mc.core.IomcCorePlugin
import io.misskey.mc.core.utils.Ticks

class NotificationHandler : Listener {
    @EventHandler
    fun onPlayerJoined(e: PlayerJoinEvent) {
        object : BukkitRunnable() {
            override fun run() {
                NotificationModule.pushNotificationTo(e.player)
            }
        }.runTaskLater(IomcCorePlugin.instance, Ticks.from(5.0).toLong())
    }
}
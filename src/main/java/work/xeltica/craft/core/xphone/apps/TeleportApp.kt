package work.xeltica.craft.core.xphone.apps

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import work.xeltica.craft.core.gui.Gui
import work.xeltica.craft.core.gui.MenuItem
import work.xeltica.craft.core.stores.WorldStore
import java.util.function.Consumer

/**
 * テレポートアプリ
 * @author Ebise Lutica
 */
class TeleportApp : AppBase() {
    override fun getName(player: Player): String = "テレポート"

    override fun getIcon(player: Player): Material = Material.COMPASS

    override fun onLaunch(player: Player) {
        val list = ArrayList<MenuItem>()
        val currentWorldName: String = player.world.name

        if (currentWorldName != "hub2") {
            list.add(MenuItem("ロビー", { player.performCommand("hub") }, Material.NETHERITE_BLOCK))
        }

        if (WorldStore.getInstance().getRespawnWorld(currentWorldName) != null) {
            list.add(MenuItem("初期スポーン", { player.performCommand("respawn") }, Material.FIREWORK_ROCKET))
            list.add(MenuItem("ベッド", { player.performCommand("respawn bed") }, Material.RED_BED))
        }

        if ("main" == currentWorldName) {
            list.add(MenuItem("ワイルドエリアBへ行く", { i: MenuItem? ->
                val loc: Location = player.location
                val x = loc.blockX * 16
                val z = loc.blockZ * 16
                player.sendMessage("ワールドを準備中です…。そのまましばらくお待ちください。")
                player.world.getChunkAtAsync(x, z)
                    .thenAccept(Consumer chunkLoaded@{
                        val wildareab = Bukkit.getWorld("wildareab")
                        if (wildareab == null) {
                            Gui.getInstance().error(player, "テレポートに失敗しました。ワールドが作成されていないようです。")
                            return@chunkLoaded
                        }
                        val y = wildareab.getHighestBlockYAt(x, z) + 1
                        val land =
                            Location(wildareab, x.toDouble(), (y - 1).toDouble(), z.toDouble())
                        if (land.block.type == Material.WATER) {
                            land.block.type = Material.STONE
                        }
                        player.teleportAsync(Location(wildareab, x.toDouble(), y.toDouble(), z.toDouble()))
                    })
            }, Material.GRASS_BLOCK))
        } else if ("wildareab" == currentWorldName) {
            list.add(
                MenuItem("メインワールドに帰る", { WorldStore.getInstance().teleportToSavedLocation(player, "main") }, Material.CREEPER_HEAD)
            )
        }

        Gui.getInstance().openMenu(player, "テレポート", list)
    }
}
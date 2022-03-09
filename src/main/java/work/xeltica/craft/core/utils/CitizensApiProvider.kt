package work.xeltica.craft.core.utils

import net.citizensnpcs.api.CitizensAPI
import org.bukkit.Bukkit
import org.bukkit.entity.Entity

class CitizensApiProvider {
    companion object {
        /**
         * Citizensプラグイン由来のNPCであるかどうかを取得します。
         */
        @JvmStatic
        fun Entity.isCitizensNpc(): Boolean {
            // Citizens が読み込まれていなければ常にfalse
            if (Bukkit.getPluginManager().getPlugin("Citizens") == null) return false

            return CitizensAPI.getNPCRegistry().isNPC(this)
        }
    }
}
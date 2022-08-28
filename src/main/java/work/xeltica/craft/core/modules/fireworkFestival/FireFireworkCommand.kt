package work.xeltica.craft.core.modules.fireworkFestival

import org.bukkit.FireworkEffect

data class FireFireworkCommand(
    val type: FireworkEffect.Type,
    val colors: List<String> = listOf("RANDOM"),
    val flicker: Boolean = false,
    val trail: Boolean = false,
    val loc: List<Double> = listOf(0.0, 0.0, 0.0),
    val random: Int = 0,
    val clone: Int = 0,
    val cloneTick: Int = 0,
) : FireworkCommandBase()
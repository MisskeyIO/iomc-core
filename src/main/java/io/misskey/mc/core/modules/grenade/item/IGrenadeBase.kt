package io.misskey.mc.core.modules.grenade.item

interface IGrenadeBase {
    fun getName(): String
    fun throwGrenade()
    fun explode()
    fun kill()
    fun hit()
}
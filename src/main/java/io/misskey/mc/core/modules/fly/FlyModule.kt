package io.misskey.mc.core.modules.fly

import io.misskey.mc.core.IomcCorePlugin
import io.misskey.mc.core.api.ModuleBase

/**
 * 飛行中にパーティクルを表示する機能を提供します。
 */
object FlyModule : ModuleBase() {
    override fun onEnable() {
        FlyingObserver().runTaskTimer(IomcCorePlugin.instance, 0, 4)
    }
}
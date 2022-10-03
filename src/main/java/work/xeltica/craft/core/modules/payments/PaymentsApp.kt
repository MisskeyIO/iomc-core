package work.xeltica.craft.core.modules.payments

import org.bukkit.Material
import org.bukkit.entity.Player
import work.xeltica.craft.core.gui.Gui
import work.xeltica.craft.core.gui.MenuItem
import work.xeltica.craft.core.stores.EbiPowerStore
import work.xeltica.craft.core.xphone.apps.AppBase

class PaymentsApp : AppBase() {
    override fun getName(player: Player): String = "EbiPay™"

    override fun getIcon(player: Player): Material = Material.EMERALD_BLOCK

    override fun onLaunch(player: Player) {
        Gui.getInstance().openMenu(player, "EbiPay - 残高 ${EbiPowerStore.getInstance().get(player)}",
            MenuItem("他プレイヤーに送る", { choosePlayer(player) }, Material.LIME_DYE),
            MenuItem("他プレイヤーに請求（開発中）", {  }, Material.LIGHT_GRAY_DYE),
        )
    }

    private fun choosePlayer(player: Player) {
        Gui.getInstance().openPlayersMenu(player, "エビパワーを送るプレイヤーは？") { target ->
            Gui.getInstance().openTextInput(player, "送信するエビパワー値を入力してください。") { amountString ->
                val amount = amountString.toIntOrNull()
                if (amount == null || amount <= 0) {
                    Gui.getInstance().error(player, "正しい数値を入力する必要があります。")
                    return@openTextInput
                }
                if (EbiPowerStore.getInstance().get(player) < amount) {
                    Gui.getInstance().error(player, "エビパワーが足りません！")
                    return@openTextInput
                }
                PaymentsModule.pay(player, target, amount)
            }
        }
    }
}
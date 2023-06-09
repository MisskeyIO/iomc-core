package io.misskey.mc.core.gui

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta
import org.bukkit.scheduler.BukkitRunnable
import io.misskey.mc.core.IomcCorePlugin
import io.misskey.mc.core.api.commands.CommandRegistry
import java.util.ArrayDeque
import java.util.UUID
import java.util.function.Consumer

/**
 * iomcCore GUI API。メニューやダイアログの表示、入力受付などを実現します。
 */
class Gui : Listener {
    companion object {
        private lateinit var instance: Gui

        /**
         * GUI APIのインスタンスを取得します。
         */
        fun getInstance(): Gui {
            return instance
        }

        fun onEnable() {
            instance = Gui()
            CommandRegistry.register("__core_gui_event__", CommandIomcCoreGuiEvent())
            Bukkit.getPluginManager().registerEvents(instance, IomcCorePlugin.instance)
        }

        private const val NEW_PAGE_CODE = "_NEW_PAGE_"
    }

    private val invMap: HashMap<Inventory, List<MenuItem>> = HashMap()
    private val bookHandlersMap: HashMap<String, HandlerTuple> = HashMap()
    private val bookSet: HashSet<BookMeta> = HashSet()
    private val chatHandlersMap: HashMap<Player, Consumer<String>> = HashMap()

    /**
     * メニューを開きます。
     * @param player メニューを開くプレイヤー
     * @param title メニューのタイトル
     * @param items メニューのアイテム
     */
    fun openMenu(player: Player, title: String, vararg items: MenuItem) {
        openMenu(player, title, listOf(*items))
    }

    /**
     * メニューを開きます。
     * @param player メニューを開くプレイヤー
     * @param title メニューのタイトル
     * @param items メニューのアイテム
     */
    fun openMenu(player: Player, title: String, items: Collection<MenuItem>) {
        openMenuJavaImpl(player, title, items.toList())
    }

    /**
     * ダイアログを開きます。
     * @param player ダイアログを開くプレイヤー
     * @param title ダイアログのタイトル
     * @param content ダイアログに記載する文字列
     */
    fun openDialog(player: Player, title: String, content: String) {
        openDialog(player, title, content, null)
    }

    /**
     * ダイアログを開きます。
     * @param player ダイアログを開くプレイヤー
     * @param title ダイアログのタイトル
     * @param content ダイアログに記載する文字列
     * @param callback UIダイアログのボタンを押したときに発火するイベント
     */
    fun openDialog(player: Player, title: String, content: String, callback: Consumer<DialogEventArgs>?) {
        openDialog(player, title, content, callback, null)
    }

    /**
     * ダイアログを開きます。
     * @param player ダイアログを開くプレイヤー
     * @param title ダイアログのタイトル
     * @param content ダイアログに記載する文字列
     * @param callback UIダイアログのボタンを押したときに発火するイベント
     * @param okButtonText OKボタンのテキスト
     */
    fun openDialog(player: Player, title: String, content: String, callback: Consumer<DialogEventArgs>?, okButtonText: String?) {
        val okText = okButtonText ?: "OK"

        openDialogJavaImpl(player, title, content, callback, okText)
    }

    /**
     * [player] に対し、[title]という名前で文字列を入力させます。
     * 入力後に [responseHandler] が実行されます。
     *
     * Java版ではチャット欄、統合版ではFormが呼ばれます。
     */
    fun openTextInput(player: Player, title: String, responseHandler: Consumer<String>?) {
        openTextInputJavaImplChat(player, title, responseHandler)
    }

    /**
     * Java Editionにてボタンを押下したときに実行される内部コマンドの処理を行います。
     * 直接呼び出さないこと。
     * @param id ID
     */
    fun handleCommand(id: String?) {
        val t = bookHandlersMap[id] ?: return
        t.handler.accept(t.eventArgs)
        bookHandlersMap.remove(id)
    }

    /**
     * ブーリアン値に対応するアイコンを取得します
     * @param flag アイコンとなる値
     * @return 対応するアイコン
     */
    fun getIconOfFlag(flag: Boolean): Material {
        return if (flag) Material.LIME_DYE else Material.GRAY_DYE
    }

    /**
     * エラーをプレイヤーに表示します。
     * @param player エラーを表示させるプレイヤー
     * @param message エラー内容
     * @return 常にtrue。コマンドの返り値に使うことを想定。
     */
    fun error(player: Player, message: String): Boolean {
        player.sendMessage(message)
        player.playSound(player.location, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 0.5f)
        return true
    }

    /**
     * 指定したプレイヤーの位置でサウンドを再生します。
     * @param player プレイヤー
     * @param sound 効果音
     * @param volume ボリューム
     * @param pitch ピッチ
     */
    fun playSound(player: Player, sound: Sound, volume: Float, pitch: SoundPitch) {
        if (player.gameMode == GameMode.SPECTATOR) {
            return
        }

        player.world.playSound(player.location, sound, SoundCategory.PLAYERS, volume, pitch.pitch)
    }

    /**
     * 指定したプレイヤーにのみサウンドを再生します。
     * @param player プレイヤー
     * @param sound 効果音
     * @param volume ボリューム
     * @param pitch ピッチ
     */
    fun playSoundLocally(player: Player, sound: Sound, volume: Float, pitch: SoundPitch) {
        player.playSound(player.location, sound, SoundCategory.PLAYERS, volume, pitch.pitch)
    }

    /**
     * 指定したプレイヤーの位置で指定Tick後にサウンドを再生します。
     * @param player プレイヤー
     * @param sound 効果音
     * @param volume ボリューム
     * @param pitch ピッチ
     * @param delay Tick
     */
    fun playSoundAfter(player: Player, sound: Sound, volume: Float, pitch: SoundPitch, delay: Int) {
        if (player.gameMode == GameMode.SPECTATOR) {
            return
        }

        Bukkit.getScheduler().runTaskLater(
            IomcCorePlugin.instance,
            Runnable { playSound(player, sound, volume, pitch) }, delay.toLong()
        )
    }

    /**
     * 指定したプレイヤーにのみ指定Tick後にサウンドを再生します。
     * @param player プレイヤー
     * @param sound 効果音
     * @param volume ボリューム
     * @param pitch ピッチ
     * @param delay Tick
     */
    fun playSoundLocallyAfter(player: Player, sound: Sound, volume: Float, pitch: SoundPitch, delay: Int) {
        Bukkit.getScheduler().runTaskLater(
            IomcCorePlugin.instance,
            Runnable { playSoundLocally(player, sound, volume, pitch) }, delay.toLong()
        )
    }

    /**
     * JavaでインベントリをメニューUIとして使うため、そのハンドリングを行います。
     * @param e ハンドリングに使用するイベント
     */
    @EventHandler
    fun onInventoryClick(e: InventoryClickEvent) {
        val inv = e.inventory
        val p = e.whoClicked

        if (!invMap.contains(inv)) return
        e.isCancelled = true

        val menuItems = invMap[inv] ?: return
        val id = e.rawSlot

        if (menuItems.size <= id || id < 0) return
        p.closeInventory()
        menuItems[id].onClick?.accept(menuItems[id])
    }

    /**
     * JavaでインベントリをメニューUIとして使うため、そのハンドリングを行います。
     * @param e ハンドリングに使用するイベント
     */
    @EventHandler
    fun onInventoryClose(e: InventoryCloseEvent) {
        val inv = e.inventory

        //管理インベントリでなければ無視
        if (!invMap.containsKey(inv)) return

        // GC
        invMap.remove(inv)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerChat(e: AsyncPlayerChatEvent) {
        if (chatHandlersMap.containsKey(e.player)) {
            e.isCancelled = true
            val handler = chatHandlersMap[e.player] ?: return
            chatHandlersMap.remove(e.player)
            object : BukkitRunnable() {
                override fun run() = handler.accept(e.message)
            }.runTask(IomcCorePlugin.instance)
        }
    }

    private fun openMenuJavaImpl(player: Player, title: String, items: List<MenuItem>) {
        val inv = Bukkit.createInventory(null, (1 + items.size / 9) * 9, Component.text(title))

        items.map {
            val item = it.icon
            if (it.isShiny) {
                item.addUnsafeEnchantment(Enchantment.DURABILITY, 1)
            }
            val meta = item.itemMeta
            meta.displayName(Component.text(it.name))
            item.itemMeta = meta
            inv.addItem(item)
            return@map
        }

        invMap[inv] = items
        player.openInventory(inv)
    }

    private fun openDialogJavaImpl(
        player: Player, title: String, content: String,
        callback: Consumer<DialogEventArgs>?, okButtonText: String,
    ) {
        val book = ItemStack(Material.WRITTEN_BOOK)
        val meta = book.itemMeta as BookMeta

        val handleString = UUID.randomUUID().toString().replace("-", "")

        val comTitle = Component.text(title + "\n\n")
        val comOkButton = Component.text(
            "\n\n" + okButtonText,
            Style.style(TextColor.color(0, 0, 0), TextDecoration.BOLD, TextDecoration.UNDERLINED)
                .clickEvent(ClickEvent.runCommand("/__core_gui_event__ $handleString"))
        )

        val queue = ArrayDeque(content.split(NEW_PAGE_CODE))

        val pages = mutableListOf<Component>()
        pages.add(comTitle.append(Component.text(queue.poll())))

        while (queue.isNotEmpty()) {
            pages.add(Component.text(queue.poll()))
        }

        pages[pages.lastIndex] = pages.last().append(comOkButton)

        for (page in pages) {
            meta.addPages(page)
        }

        meta.author = "Misskey.io Minecraft"
        meta.title = title

        book.itemMeta = meta
        bookSet.add(meta)
        player.openBook(book)

        if (callback != null) {
            bookHandlersMap[handleString] = HandlerTuple(callback, DialogEventArgs(player), meta)
        }
    }

    private fun openTextInputJavaImplChat(player: Player, title: String, responseHandler: Consumer<String>?) {
        player.sendMessage(ChatColor.BOLD.toString() + title)
        player.sendMessage(ChatColor.GRAY.toString() + "チャット欄に値を入力してください:")
        playSoundLocally(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, SoundPitch.F_1)
        playSoundLocallyAfter(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, SoundPitch.D_2, 8)
        chatHandlersMap[player] = responseHandler ?: Consumer<String> {}
    }

    data class HandlerTuple(val handler: Consumer<DialogEventArgs>, val eventArgs: DialogEventArgs, val meta: BookMeta)
}

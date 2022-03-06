package work.xeltica.craft.core.handlers

import de.tr7zw.nbtapi.NBTEntity
import de.tr7zw.nbtapi.NBTItem
import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockDispenseEvent
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.player.PlayerEggThrowEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import work.xeltica.craft.core.XCorePlugin
import work.xeltica.craft.core.gui.Gui
import work.xeltica.craft.core.stores.MobBallStore
import work.xeltica.craft.core.utils.CitizensApiProvider.Companion.isCitizensNpc
import java.util.*

class MobBallHandler : Listener {
    @EventHandler
    fun onPlayerThrowMobBall(e: PlayerEggThrowEvent) {
        if (!MobBallStore.getInstance().isMobBall(e.egg.item)) return

        e.isHatching = false
    }

    @EventHandler
    fun onMobBallHitEntity(e: ProjectileHitEvent) {
        val egg = e.entity as? Egg ?: return
        val player = egg.shooter as? Player ?: return
        if (!MobBallStore.getInstance().isMobBall(egg.item)) return

        egg.remove()
        e.isCancelled = true

        val target = e.hitEntity
        // モブにぶつからなかった場合はボールを返却する
        if (target !is Mob) {
            dropEgg(egg, player)
            return
        }

        // Citizens NPCであればボールを返却する
        if (target.isCitizensNpc()) {
            dropEgg(egg, player)
            return
        }

        val ownerId = if (target is Tameable) target.ownerUniqueId else null
        // 飼育可能かつ飼育済みかつ親IDがあり親が自分でなければはボールを返却する
        if (ownerId is UUID && ownerId != player.uniqueId) {
            dropEgg(egg, player, "このモブは他人のペットです。人のモブを獲ったら泥棒！")
            return
        }

        if (target.persistentDataContainer.has(NamespacedKey(XCorePlugin.getInstance(), "isCaptured"), PersistentDataType.INTEGER)) {
            dropEgg(egg, player, "そのモブは既に捕獲されています。")
            return
        }

        val material = getSpawnEggMaterial(target.type)
        val isTamedByMe = target is Tameable && target.ownerUniqueId != null && target.ownerUniqueId!! == player.uniqueId
        var i = 1
        val spawnEgg = ItemStack(material, 1)
        val eggNbt = restoreMob(target, spawnEgg)
        val eggEntity = egg.world.dropItem(egg.location, spawnEgg)
        // 自分のペットであれば100%捕獲に成功する
        val difficulty = if (isTamedByMe) 100 else MobBallStore.getInstance().calculate(target)
        var isGotcha = false

        eggEntity.setCanMobPickup(false)
        eggEntity.setCanPlayerPickup(false)
        object: BukkitRunnable() {
            override fun run() {
                if (i % 20 == 0) {
                    val randNum = random.nextInt(100)
                    isGotcha = randNum < difficulty
                    i = if (isGotcha) i else 80
                    eggEntity.world.playSound(eggEntity.location, Sound.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS, 1f, 1.5f)
                    if (i < 80) {
                        showWaitingParticle(eggEntity.location)
                    }
                }
                i++
                if (i > 80) {
                    this.cancel()
                    if (isGotcha) {
                        player.playSound(egg.location, Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1f, 0.5f)
                        player.sendMessage("§a§lおめでとう！§r${eggNbt.getString("mobCase")}を捕まえた！")
                        showSuccessParticle(eggEntity.location)
                        eggEntity.setCanPlayerPickup(true)
                    } else {
                        egg.world.playSound(egg.location, Sound.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1f, 1f)
                        val entityTag = eggNbt.getCompound("EntityTag")
                        entityTag.removeKey("Pos")
                        val type = EntityType.valueOf(eggNbt.getString("mobType"))
                        eggEntity.world.spawnEntity(eggEntity.location, type, CreatureSpawnEvent.SpawnReason.CUSTOM) {
                            NBTEntity(it).mergeCompound(entityTag)
                            it.world.playSound(it.location, Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1f, 1f)
                            showTeleportParticle(it.location)
                            player.sendMessage("残念！ボールから出てきてしまった…。")
                            eggEntity.remove()
                        }
                    }
                }
            }
        }.runTaskTimer(XCorePlugin.getInstance(), 0, 1L)
    }

    @EventHandler
    fun onReleaseMob(e: PlayerInteractEvent) {
        val item = e.item ?: return
        val block = e.clickedBlock ?: return
        val nbt = NBTItem(item)
        if (!nbt.hasKey("mobCase")) return
        e.isCancelled = true

        if (!nbt.getBoolean("isActive")) {
            return
        }
        val entityTag = nbt.getCompound("EntityTag")
        entityTag.removeKey("Pos")
        nbt.applyNBT(item)
        val type = EntityType.valueOf(nbt.getString("mobType"))
        e.player.world.spawnEntity(block.location, type, CreatureSpawnEvent.SpawnReason.CUSTOM) {
            NBTEntity(it).mergeCompound(entityTag)
            it.teleport(block.location.add(0.0, 1.0, 0.0))
            it.world.playSound(it.location, Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1f, 1f)
            it.persistentDataContainer.set(NamespacedKey(XCorePlugin.getInstance(), "isCaptured"), PersistentDataType.INTEGER, 1)
            showTeleportParticle(it.location)
        }
        nbt.setBoolean("isActive", false)
        nbt.applyNBT(item)
        MobBallStore.getInstance().setMobCaseMeta(item)
        e.isCancelled = true
    }

    @EventHandler
    fun onRestoreMob(e: PlayerInteractEntityEvent) {
        val p = e.player
        val entity = e.rightClicked as? Mob ?: return
        val item = p.inventory.itemInMainHand
        val nbt = NBTItem(item)
        if (!nbt.hasKey("mobCase")) return
        e.isCancelled = true

        if (nbt.getBoolean("isActive")) {
            return
        }

        val entityTag = nbt.getCompound("EntityTag")
        if (!entityTag.getUUID("UUID").equals(entity.uniqueId)) {
            Gui.getInstance().error(p, "そのモブはモブケースに適合していません。")
            return
        }

        restoreMob(entity, item)
    }

    @EventHandler
    fun onUseMobBallInDispenser(e: BlockDispenseEvent) {
        val item = e.item
        val nbt = NBTItem(item)
        if (!nbt.hasKey("mobCase")) return
        // ディスペンサーでのモブケースの使用を禁止する
        e.isCancelled = true
    }

    private fun restoreMob(target: Mob, spawnEgg: ItemStack): NBTItem {
        val targetNbt = NBTEntity(target)
        val eggNbt = NBTItem(spawnEgg)
        eggNbt.removeKey("EntityTag")
        val entityTag = eggNbt.addCompound("EntityTag")
        entityTag.mergeCompound(targetNbt)
        entityTag.removeKey("Pos")
        eggNbt.setBoolean("isActive", true)
        eggNbt.setString("mobType", target.type.name)
        eggNbt.setString("mobCase", target.customName ?: target.name)
        eggNbt.applyNBT(spawnEgg)
        MobBallStore.getInstance().setMobCaseMeta(spawnEgg)
        target.remove()
        target.world.playSound(target.location, Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1f, 1f)
        showTeleportParticle(target.location)
        return eggNbt
    }

    private fun getSpawnEggMaterial(type: EntityType): Material {
        // ムーシュルームだけ名前が:tokushu:なので特殊処理
        if (type == EntityType.MUSHROOM_COW) return Material.MOOSHROOM_SPAWN_EGG
        return Material.values().firstOrNull { it.name == "${type.name}_SPAWN_EGG" } ?: Material.CHICKEN_SPAWN_EGG
    }

    private fun dropEgg(egg: Egg, player: Player, string: String? = null) {
        egg.world.dropItem(egg.location, egg.item)
        egg.world.playSound(egg.location, Sound.ITEM_SHIELD_BLOCK, SoundCategory.PLAYERS, 1f, 0.5f)
        if (string != null) Gui.getInstance().error(player, string)
    }

    private fun showWaitingParticle(loc: Location) {
        loc.world.spawnParticle(Particle.SMOKE_NORMAL, loc, 64, 0.3, 0.0, 0.3, 0.1)
    }

    private fun showSuccessParticle(loc: Location) {
        loc.world.spawnParticle(Particle.COMPOSTER, loc, 64, 0.3, 0.5, 0.3, 0.1)
    }

    private fun showTeleportParticle(loc: Location) {
        loc.world.spawnParticle(Particle.PORTAL, loc, 64, 1.4, 1.4, 1.4, 0.7)
    }

    private val random = Random()
}
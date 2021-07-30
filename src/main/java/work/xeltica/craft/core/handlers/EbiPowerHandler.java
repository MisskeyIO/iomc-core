package work.xeltica.craft.core.handlers;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.Tag;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Hoglin;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import net.kyori.adventure.text.Component;
import work.xeltica.craft.core.models.Hint;
import work.xeltica.craft.core.models.PlayerDataKey;
import work.xeltica.craft.core.stores.EbiPowerStore;
import work.xeltica.craft.core.stores.HintStore;
import work.xeltica.craft.core.stores.PlayerStore;

public class EbiPowerHandler implements Listener{
    public EbiPowerHandler() {
        epBlackList.add("hub");
        epBlackList.add("hub2");
        epBlackList.add("sandbox");
        epBlackList.add("art");
        epBlackList.add("sandbox2");
        epBlackList.add("pvp");
        epBlackList.add("hub_dev");
        epBlackList.add("world");
        epBlackList.add("world_nether");
        epBlackList.add("world_the_end");
        epBlackList.add("wildarea");
        epBlackList.add("travel_wildarea");
        epBlackList.add("travel_megawild");
        epBlackList.add("travel_maikura_city");

        crops.addAll(Tag.CROPS.getValues());
    }

    @EventHandler
    public void onCombat(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player killer && e.getEntity() instanceof LivingEntity victim) {
            if (playerIsInBlacklisted(killer)) return;
            // スポナーは対象外
            if (victim.fromMobSpawner()) return;
            // TODO: TT登録可能にしてその中のキルは対象外にする

            if (victim instanceof Cat || victim instanceof Ocelot) {
                store().tryTake(killer, 100);
                notification(killer, "可愛い可愛いネコちゃんを殴るなんて！100EPを失った。");
                killer.playSound(killer.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.PLAYERS, 0.7f, 0.5f);
                HintStore.getInstance().achieve(killer, Hint.VIOLENCE_CAT);
            } else if (victim instanceof Tameable pet && pet.isTamed()) {
                store().tryTake(killer, 10);
                notification(killer, "ペットを殴るなんて！10EPを失った。");
                killer.playSound(killer.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.PLAYERS, 0.7f, 0.5f);
                HintStore.getInstance().achieve(killer, Hint.VIOLENCE_PET);
            } else if (victim instanceof Ageable man && !(victim instanceof Monster) && !(victim instanceof Hoglin) && !man.isAdult()) {
                store().tryTake(killer, 10);
                notification(killer, "子供を殴るなんて！10EPを失った。");
                killer.playSound(killer.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, SoundCategory.PLAYERS, 0.7f, 0.5f);
                HintStore.getInstance().achieve(killer, Hint.VIOLENCE_CHILD);
            } else {
                var buff = getMobDropBonus(killer.getInventory().getItemInMainHand()) * 4;
                var power = 3 + (buff > 0 ? random.nextInt(buff) : 0);

                if ("nightmare2".equals(killer.getWorld().getName())) {
                    power *= 2;
                }
                if (power > 0) {
                    store().tryGive(killer, power);
                }
            }
        }
    }

    @EventHandler
    public void on(PlayerAdvancementDoneEvent e) {
        var p = e.getPlayer();
        if (playerIsInBlacklisted(p)) return;
        store().tryGive(p, ADVANCEMENT_POWER);
    }

    @EventHandler
    public void on(PlayerJoinEvent e) {
        var now = new Date();
        var ps = PlayerStore.getInstance();
        var record = ps.open(e.getPlayer());
        var prev = new Date(record.getLong(PlayerDataKey.LAST_JOINED, now.getTime()));
        if (prev.getYear() != now.getYear() && prev.getMonth() != now.getMonth() && prev.getDate() != now.getDate()) {
            store().tryGive(e.getPlayer(), LOGIN_BONUS_POWER);
            notification(e.getPlayer(), "ログボ達成！" + LOGIN_BONUS_POWER + "EPを獲得。");
        }
        record.set(PlayerDataKey.LAST_JOINED, now.getTime());
    }

    @EventHandler
    public void on(BlockBreakEvent e) {
        var p = e.getPlayer();
        if (playerIsInBlacklisted(p)) return;
        if (e.getBlock().getBlockData() instanceof org.bukkit.block.data.Ageable a && a.getAge() == a.getMaximumAge()) {
            var power = (1 + getBlockDropBonus(e.getPlayer().getInventory().getItemInMainHand())) * HARVEST_POWER_MULTIPLIER;
            store().tryGive(p, power);
        }
    }

    private void notification(Player p, String mes) {
        p.sendActionBar(Component.text(mes));
        // p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1, 2);
    }

    private boolean playerIsInBlacklisted(Player p) {
        var wName = p.getWorld().getName();
        return epBlackList.contains(wName);
    }

    private EbiPowerStore store() {
        return EbiPowerStore.getInstance();
    }

    private int getMobDropBonus(ItemStack stack) {
        if (stack == null) return 0;
        return stack.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
    }

    private int getBlockDropBonus(ItemStack stack) {
        if (stack == null) return 0;
        return stack.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
    }

    private HashSet<String> epBlackList = new HashSet<>();

    private HashSet<Material> crops = new HashSet<>();

    private static final int ADVANCEMENT_POWER = 30;
    private static final int HARVEST_POWER_MULTIPLIER = 1;
    private static final int LOGIN_BONUS_POWER = 50;

    private static final Random random = new Random();
}

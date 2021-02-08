package work.xeltica.craft.otanoshimiplugin.handlers;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import work.xeltica.craft.otanoshimiplugin.OmikujiScore;
import work.xeltica.craft.otanoshimiplugin.OmikujiStore;

public class PlayerHandler implements Listener {
    public PlayerHandler(Plugin p) {
        this.plugin = p;
    }
    @EventHandler
    public void onPlayerDeath(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player))
            return;
        var p = (Player)e.getEntity();
        if (p.getHealth() - e.getFinalDamage() > 0)
            return;

        var score = OmikujiStore.getInstance().get(p);
        var th = 
            score == OmikujiScore.Tokudaikichi ? 5 : 
            score == OmikujiScore.Daikichi ? 1 : 0;
        
        if ((int)(Math.random() * 100) >= th) return;

        var i = p.getInventory();
        var heldItem = i.getItemInMainHand();
        i.remove(heldItem);
        i.setItemInMainHand(new ItemStack(Material.TOTEM_OF_UNDYING));
        new BukkitRunnable(){
            @Override
            public void run() {
                i.setItemInMainHand(heldItem);
            }
        }.runTaskLater(this.plugin, 1);
    }
    private Plugin plugin;
}

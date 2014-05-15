package org.riking.skyblock;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    private CommunitySkyblock plugin;

    public PlayerListener(CommunitySkyblock plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (!event.getDrops().isEmpty()) {
            plugin.getDepositor().depositItems(event.getEntity().getWorld(), event.getDrops());
            event.getDrops().clear();
            event.setDeathMessage(ChatColor.YELLOW + event.getEntity().getDisplayName() + ChatColor.AQUA + " died and their items were returned to the chest stack.");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        plugin.getDepositor().depositItems(player.getWorld(), player.getInventory().getContents());
        player.getInventory().clear();
        plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
            public void run() {
                plugin.getServer().broadcastMessage(ChatColor.YELLOW + player.getDisplayName() + ChatColor.AQUA + " quit and their items were returned to the chest stack.");
            }
        });
    }
}

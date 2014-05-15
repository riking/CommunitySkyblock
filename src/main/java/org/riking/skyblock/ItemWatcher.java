package org.riking.skyblock;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemWatcher implements Listener {
    CommunitySkyblock plugin;
    public ItemWatcher(CommunitySkyblock plugin) {
        this.plugin = plugin;
        new ItemWatchingTask().runTaskTimer(plugin, 0, 4);
    }

    private List<Item> watchedItemsList = new ArrayList<Item>();

    @EventHandler
    public void trackItemSpawn(ItemSpawnEvent event) {
        watchedItemsList.add(event.getEntity());
    }

    @EventHandler
    public void untrackItemPickup(PlayerPickupItemEvent event) {
        watchedItemsList.remove(event.getItem());
    }

    public class ItemWatchingTask extends BukkitRunnable {
        public void run() {
            Iterator<Item> iter = watchedItemsList.iterator();
            while (iter.hasNext()) {
                Item item = iter.next();

                if (!item.isValid()) {
                    iter.remove();
                    continue;
                }

                if (item.getLocation().getY() < 0) {
                    plugin.getServer().broadcastMessage(ChatColor.AQUA + "Recovered a dropped " + item.getItemStack().getType());
                    plugin.getDepositor().depositItems(item.getWorld(), item.getItemStack());
                    item.remove();
                } else {
                    System.out.println("Checked item at y=" + item.getLocation().getY());
                }
            }

            World world = plugin.getServer().getWorlds().get(0);
            Entity temporary = world.spawnArrow(new Location(world, 0, -10, 0), new Vector(0, 2, 0), 1, 0);

            List<Entity> nearbyEntities = temporary.getNearbyEntities(500, 30, 500);
            for (Entity entity : nearbyEntities) {
                if (entity instanceof Item) {
                    Item item = (Item) entity;
                    if (item.getLocation().getY() < 0) {
                        plugin.getServer().broadcastMessage(ChatColor.AQUA + "Recovered a dropped " + item.getItemStack().getType());
                        plugin.getDepositor().depositItems(item.getWorld(), item.getItemStack());
                        item.remove();
                    } else {
                        System.out.println("Checked item at y=" + item.getLocation().getY());
                    }
                }
            }

            temporary.remove();
        }
    }
}

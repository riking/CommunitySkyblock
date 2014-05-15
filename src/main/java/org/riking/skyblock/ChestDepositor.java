package org.riking.skyblock;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ChestDepositor {
    private static final Location startLocation = new Location(null, 0, 64, 0);

    public void depositItems(World world, ItemStack... inItems) {
        List<ItemStack> itemCopy = new ArrayList<ItemStack>();

        for (ItemStack i : inItems) {
            if (i != null) {
                itemCopy.add(i);
            }
        }

        depositItems(world, itemCopy);
    }

    public void depositItems(World world, Collection<ItemStack> inItems) {
        startLocation.setWorld(world);
        Block block;
        Chest chest;
        block = world.getBlockAt(startLocation);

        List<ItemStack> items = new ArrayList<ItemStack>(inItems);

        while (!items.isEmpty()) {
            if (block.getType() != Material.CHEST) {
                world.playSound(block.getLocation(), Sound.ANVIL_LAND, 10, 1);
                Bukkit.broadcast(ChatColor.DARK_AQUA + "Another chest has been added!", Server.BROADCAST_CHANNEL_USERS);
                block.setType(Material.CHEST);
            }
            chest = (Chest) block.getState();
            Inventory inv = chest.getBlockInventory();

            HashMap<Integer, ItemStack> unfilled = inv.addItem(items.toArray(new ItemStack[items.size()]));
            items.clear();
            items.addAll(unfilled.values());

            if (block.getY() >= 250) {
                Bukkit.broadcast(ChatColor.DARK_RED + "The chest stack is full! Items were lost!", Server.BROADCAST_CHANNEL_USERS);
                return;
            }
            block = block.getRelative(BlockFace.UP);
        }
    }
}

package org.riking.skyblock;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CommunitySkyblock extends JavaPlugin {

    private ChestDepositor depositor;

    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        depositor = new ChestDepositor();

        pm.registerEvents(new PlayerListener(this), this);
        pm.registerEvents(new ItemWatcher(this), this);
    }

    public ChestDepositor getDepositor() {
        return depositor;
    }
}

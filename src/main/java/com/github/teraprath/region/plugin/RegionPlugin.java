package com.github.teraprath.region.plugin;

import com.github.teraprath.region.api.RegionAPI;
import com.github.teraprath.region.api.RegionExtension;
import com.github.teraprath.region.command.RegionCommand;
import com.github.teraprath.region.listener.RegionListener;
import com.github.teraprath.region.manager.SelectionManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.UUID;

public final class RegionPlugin extends JavaPlugin {

    private final RegionAPI api = new RegionAPI(this);
    private final HashMap<UUID, SelectionManager> selections = new HashMap<>();

    @Override
    public void onEnable() {

        api.init();

        final PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new RegionListener(this), this);
        getCommand("region").setExecutor(new RegionCommand(this));

        if (pluginManager.getPlugin("PlaceholderAPI") != null) {
            new RegionExtension(this).register();
        }

    }

    public void register(@Nonnull UUID uuid) {
        this.selections.put(uuid, new SelectionManager());
    }

    public SelectionManager getSelection(UUID uuid) {
        return this.selections.get(uuid);
    }

    public RegionAPI getAPI() {
        return api;
    }

}

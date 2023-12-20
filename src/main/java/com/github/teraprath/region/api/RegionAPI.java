package com.github.teraprath.region.api;

import com.github.teraprath.region.manager.FileManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.*;

public class RegionAPI {

    private final JavaPlugin plugin;
    private final HashMap<String, Region> regions;
    private FileManager manager;
    private String defaultDisplayName;

    public RegionAPI(@Nonnull JavaPlugin plugin) {
        this.plugin = plugin;
        this.regions = new HashMap<>();
    }

    public RegionAPI init() {
        Plugin depend = plugin.getServer().getPluginManager().getPlugin("RegionAPI");
        if (depend != null) {
            if (plugin != depend) {
                depend.getLogger().info("Plugin '" + plugin.getName() + "' registered.");
            }
            this.regions.clear();
            this.manager = new FileManager(depend);
            reload();
        } else {
            plugin.getLogger().info("RegionsAPI is not installed! Download: https://github.com/teraprath/RegionsAPI/releases");
        }
        return this;
    }

    public Region getRegion(@Nonnull String regionName) {
        return this.regions.get(regionName);
    }

    public Collection<Region> getRegions() {
        return this.regions.values();
    }

    public void addRegion(@Nonnull Region region) {
        if (this.regions.get(region.getName()) != null) {
            plugin.getLogger().info("Region '" + region.getName() + "' already exists.");
            return;
        }
        this.regions.put(region.getName(), region);
        save();
    }



    public void deleteRegion(@Nonnull String name) {
        if (this.regions.get(name) == null) {
            plugin.getLogger().info("Region '" + name + "' does not exists.");
            return;
        }
        this.regions.remove(name);
        save();
    }

    private void save() {
        manager.save(this.regions);
        reload();
    }

    public void reload() {
        manager.reload();
        manager.getKeys().forEach(key -> {
            Region region = manager.get(key);
            if (region != null) {
                this.regions.put(key, region);
            }
        });
        this.defaultDisplayName = manager.getDefaultDisplayName();
    }

    public String getDefaultDisplayName() {
        return defaultDisplayName;
    }

}

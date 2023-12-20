package com.github.teraprath.region.manager;

import com.github.teraprath.region.api.Region;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class FileManager {

    private final Plugin plugin;
    private final File file;
    private FileConfiguration config;

    public FileManager(Plugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "regions.yml");
    }

    public void reload() {
        if (!(file.exists())) {
            plugin.saveResource("regions.yml", false);
        }

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public Set<String> getKeys() {
        return config.getKeys(false);
    }

    public Region get(String key) {
        Location pos1 = config.getLocation(key + ".pos1");
        Location pos2 = config.getLocation(key + ".pos2");
        String displayName = config.getString(key + ".displayName");
        if (pos1 != null && pos2 != null) {
            Region region = new Region(key, pos1, pos2);
            if (displayName != null) {
                region.setDisplayName(displayName);
            }
            return region;
        }
        return null;
    }

    public String getDefaultDisplayName() {
        return this.config.getString("default.displayName");
    }

    public void save(@Nonnull HashMap<String, Region> regions) {
        try {
            this.config.getKeys(false).forEach(key -> {
                this.config.set(key, null);
            });
            regions.forEach((name, region) -> {
                this.config.set(name + ".pos1", region.getPos1());
                this.config.set(name + ".pos2", region.getPos2());
                if (region.getDisplayName() != null) {
                    this.config.set(name + ".displayName", region.getDisplayName());
                }
            });
            config.save(file);
            reload();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

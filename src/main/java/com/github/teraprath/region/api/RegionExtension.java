package com.github.teraprath.region.api;

import com.github.teraprath.region.plugin.RegionPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RegionExtension extends PlaceholderExpansion {

    private final RegionPlugin plugin;

    public RegionExtension(@NotNull RegionPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "region";
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().get(0);
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        switch (params.toLowerCase()) {
            case "amount":
                return plugin.getAPI().getRegions().size() + "";
            case "current":
                for (Region region : plugin.getAPI().getRegions()) {
                    Player target = plugin.getServer().getPlayer(player.getUniqueId());
                    if (target != null) {
                        if (region.contains(target)) {
                            return region.getDisplayName();
                        }
                    }
                }
                if (plugin.getAPI().getDefaultDisplayName() != null) {
                    return plugin.getAPI().getDefaultDisplayName();
                }
                return null;
            default:
                Region region = plugin.getAPI().getRegion(params.toLowerCase());
                if (region != null) {
                    if (region.getDisplayName() != null) {
                        return region.getDisplayName();
                    } else {
                        return region.getName();
                    }
                }
                return null;
        }
    }
}

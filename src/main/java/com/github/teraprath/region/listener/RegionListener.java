package com.github.teraprath.region.listener;

import com.github.teraprath.region.plugin.RegionPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class RegionListener implements Listener {

    private final RegionPlugin plugin;


    public RegionListener(RegionPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (e.getPlayer().hasPermission("region.command")) {
            plugin.register(e.getPlayer().getUniqueId());
        }
    }
}

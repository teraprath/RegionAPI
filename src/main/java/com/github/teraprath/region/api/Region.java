package com.github.teraprath.region.api;


import org.bukkit.Location;

public class Region extends RegionCuboid {

    private final String name;

    public Region(String name, Location pos1, Location pos2) {
        super(pos1, pos2);
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
}

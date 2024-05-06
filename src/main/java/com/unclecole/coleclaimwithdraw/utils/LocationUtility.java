package com.unclecole.coleclaimwithdraw.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationUtility {

    public Location parseToLocation(String string) {
        if (string == null) return null;
        String[] data = string.split(":");
        try {
            double x = Double.parseDouble(data[0]);
            double y = Double.parseDouble(data[1]);
            double z = Double.parseDouble(data[2]);
            World world = Bukkit.getServer().getWorld(data[3]);
            return new Location(world, x, y, z);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String parseToString(Location location) {
        return location.getX() + ":" + location.getY() + ":" + location.getZ() + ":" + location.getWorld().getName();
    }

    public boolean isInRegion(String playerLoc, String corner1, String corner2) {

        String[] player = playerLoc.split(":");
        String[] cornerone = corner1.split(":");
        String[] cornertwo = corner2.split(":");

        if(!player[3].equals(cornertwo[3])) return false;

        double x = Double.parseDouble(player[0]);
        double y = Double.parseDouble(player[1]);
        double z = Double.parseDouble(player[2]);

        double lowX = Math.min(Double.parseDouble(cornerone[0]),Double.parseDouble(cornertwo[0]));
        double lowY = Math.min(Double.parseDouble(cornerone[1]),Double.parseDouble(cornertwo[1]));
        double lowZ = Math.min(Double.parseDouble(cornerone[2]),Double.parseDouble(cornertwo[2]));

        double highX = Math.max(Double.parseDouble(cornerone[0]),Double.parseDouble(cornertwo[0]));
        double highY = Math.max(Double.parseDouble(cornerone[1]),Double.parseDouble(cornertwo[1]));
        double highZ = Math.max(Double.parseDouble(cornerone[2]),Double.parseDouble(cornertwo[2]));

        if((x <= highX && x >= lowX) && (y <= highY && y >= lowY) && (z <= highZ && z >= lowZ)){
            return true;
        }
        return false;
    }

}

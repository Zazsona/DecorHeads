package com.zazsona.decorheads.blockmeta;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class BlockLocator implements Serializable
{
    private UUID worldId;
    private int x;
    private int y;
    private int z;

    public BlockLocator(Location location)
    {
        this.worldId = location.getWorld().getUID();
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
    }

    public BlockLocator(UUID worldId, int x, int y, int z)
    {
        this.worldId = worldId;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location getLocation()
    {
        return new Location(Bukkit.getWorld(worldId), x, y, z);
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof BlockLocator)
        {
            BlockLocator blockLocator = (BlockLocator) o;
            return (worldId.equals(blockLocator.worldId) && x == blockLocator.x && y == blockLocator.y && z == blockLocator.z);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(worldId, x, y, z);
    }

    @Override
    public String toString() {
        return "BlockLocation{" + "worldId=" + worldId + ",x=" + x + ",y=" + y + ",z=" + z + '}';
    }
}

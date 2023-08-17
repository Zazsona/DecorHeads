package com.zazsona.decorheads.blockmeta.library.container;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.List;

public interface IBlockPluginPropertiesContainer
{
    /**
     * Gets the number of blocks registered to the container
     * @return the count
     */
    int getBlocksCount();

    /**
     * Gets the locations for blocks that hold plugin property data
     * @return the locations of blocks with plugin properties
     */
    List<Location> getBlockLocations();

    /**
     * Gets the plugin properties for the block at the given {@link Location}
     * @param location the location of the block
     */
    HashMap<String, String> getBlockPluginProperties(Location location);
}

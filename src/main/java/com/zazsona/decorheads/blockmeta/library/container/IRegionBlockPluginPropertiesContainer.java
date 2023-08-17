package com.zazsona.decorheads.blockmeta.library.container;

import org.bukkit.Location;

public interface IRegionBlockPluginPropertiesContainer extends IBlockPluginPropertiesContainer
{

    /**
     * Gets the plugin block properties container for the region that the provided location falls in
     * @param location a location to get the region for
     * @return a container that holds block properties within the region, or null if no mapping exists.
     */
    IChunkBlockPluginPropertiesContainer getRegion(Location location);

    /**
     * Gets the plugin block properties container for the region that the provided location falls in, or if no region
     * is registered, the provided default value.
     * @param location a location to get the region for
     * @param defaultValue the default value to return if no mapping is found
     * @return the registered container that holds block properties within the region, or the provided default value if no
     * region is registered.
     */
    IChunkBlockPluginPropertiesContainer getOrDefaultRegion(Location location, IChunkBlockPluginPropertiesContainer defaultValue);

}

package com.zazsona.decorheads.blockmeta.library.container;

import org.bukkit.Location;

public interface IMutableRegionBlockPluginPropertiesContainer extends IRegionBlockPluginPropertiesContainer, IMutableBlockPluginPropertiesContainer
{
    /**
     * Sets the plugin block properties container for the region that the provided location falls in
     *
     * @param location         a location to set the region for
     * @param regionProperties the properties to map to the region
     */
    void setRegion(Location location, IMutableChunkBlockPluginPropertiesContainer regionProperties);

    /**
     * Removes the plugin block properties container for the region that the provided location falls in
     *
     * @param location a location to remove the chunk for
     * @return the removed properties container, or null if there were no properties mapped to the region
     */
    IChunkBlockPluginPropertiesContainer removeRegion(Location location);
}

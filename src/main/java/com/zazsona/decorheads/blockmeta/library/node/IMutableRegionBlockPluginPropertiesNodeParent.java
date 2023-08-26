package com.zazsona.decorheads.blockmeta.library.node;

import org.bukkit.Location;

public interface IMutableRegionBlockPluginPropertiesNodeParent extends IRegionBlockPluginPropertiesNodeParent, IMutableBlockPluginPropertiesNode
{
    /**
     * Sets the {@link RegionBlockPluginPropertiesNode}, including all its children, for the provided {@link Location}, overwriting any
     * previously set values.
     *
     * @param location a location in the region
     * @param regionNode the RegionNode to set
     */
    RegionBlockPluginPropertiesNode putRegionNode(Location location, RegionBlockPluginPropertiesNode regionNode);

    /**
     * Removes the {@link RegionBlockPluginPropertiesNode}, including all its children, for the provided {@link Location}.
     *
     * @param location a location in the region
     */
    RegionBlockPluginPropertiesNode removeRegionNode(Location location);
}

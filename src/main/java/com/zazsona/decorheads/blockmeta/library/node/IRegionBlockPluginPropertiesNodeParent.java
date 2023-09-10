package com.zazsona.decorheads.blockmeta.library.node;

import org.bukkit.Location;

public interface IRegionBlockPluginPropertiesNodeParent extends IBlockPluginPropertiesNode
{
    /**
     * Gets the {@link RegionBlockPluginPropertiesNode} for the region at the provided blockLocation.
     * @param blockLocation a block location contained within the region
     * @return the RegionNode
     */
    IChunkBlockPluginPropertiesNodeParent getRegionNode(Location blockLocation);
}

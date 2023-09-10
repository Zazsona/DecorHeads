package com.zazsona.decorheads.blockmeta.library.node;

import org.bukkit.Location;

public interface IWorldBlockPluginPropertiesNodeParent extends IBlockPluginPropertiesNode
{
    /**
     * Gets the {@link WorldBlockPluginPropertiesNode} for the world at the provided blockLocation.
     * @param blockLocation a block location with world data
     * @return the WorldNode
     */
    IRegionBlockPluginPropertiesNodeParent getWorldNode(Location blockLocation);
}

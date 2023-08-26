package com.zazsona.decorheads.blockmeta.library.node;

import org.bukkit.Location;

public interface IWorldBlockPluginPropertiesNodeParent extends IBlockPluginPropertiesNode
{
    /**
     * Gets the {@link WorldBlockPluginPropertiesNode} for the world at the provided location.
     * @param location a location with world data
     * @return the WorldNode
     */
    WorldBlockPluginPropertiesNode getWorldNode(Location location);
}

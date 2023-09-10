package com.zazsona.decorheads.blockmeta.library.node;

import org.bukkit.World;

public interface IWorldBlockPluginPropertiesNodeParent extends IBlockPluginPropertiesNode
{
    /**
     * Gets the {@link IRegionBlockPluginPropertiesNodeParent} for the world at the provided world.
     * @param world the world
     * @return the world node
     */
    IRegionBlockPluginPropertiesNodeParent getWorldNode(World world);
}

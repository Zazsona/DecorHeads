package com.zazsona.decorheads.blockmeta.library.node;

import org.bukkit.Location;
import org.bukkit.block.Block;

public interface IBlockPluginPropertiesNodeParent extends IBlockPluginPropertiesNode
{
    /**
     * Gets the {@link BlockPluginPropertiesNode} for the block at the provided location.
     * @param location a location for a {@link Block}
     * @return the BlockNode
     */
    BlockPluginPropertiesNode getBlockNode(Location location);
}

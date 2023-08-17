package com.zazsona.decorheads.blockmeta.library.node;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;

public interface IBlockNodeParent extends INode
{
    /**
     * Gets the {@link BlockNode} for the block at the provided location.
     * @param location a location for a {@link Block}
     * @return the BlockNode
     */
    BlockNode getBlockNode(Location location);
}

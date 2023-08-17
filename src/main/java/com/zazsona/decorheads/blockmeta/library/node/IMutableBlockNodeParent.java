package com.zazsona.decorheads.blockmeta.library.node;

import org.bukkit.Location;

public interface IMutableBlockNodeParent extends IBlockNodeParent, IMutableNode
{
    /**
     * Sets the {@link BlockNode}, including all its children, for the provided {@link Location}, overwriting any
     * previously set values.
     *
     * @param location the location of the chunk
     * @param blockNode the BlockNode to set
     */
    BlockNode putBlockNode(Location location, BlockNode blockNode);

    /**
     * Removes the {@link BlockNode}, including all its children, for the provided {@link Location}.
     *
     * @param location the location of the chunk
     */
    BlockNode removeBlockNode(Location location);
}

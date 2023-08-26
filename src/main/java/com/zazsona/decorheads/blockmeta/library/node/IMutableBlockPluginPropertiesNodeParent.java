package com.zazsona.decorheads.blockmeta.library.node;

import org.bukkit.Location;

public interface IMutableBlockPluginPropertiesNodeParent extends IBlockPluginPropertiesNodeParent, IMutableBlockPluginPropertiesNode
{
    /**
     * Sets the {@link BlockPluginPropertiesNode}, including all its children, for the provided {@link Location}, overwriting any
     * previously set values.
     *
     * @param location the location of the chunk
     * @param blockNode the BlockNode to set
     */
    BlockPluginPropertiesNode putBlockNode(Location location, BlockPluginPropertiesNode blockNode);

    /**
     * Removes the {@link BlockPluginPropertiesNode}, including all its children, for the provided {@link Location}.
     *
     * @param location the location of the chunk
     */
    BlockPluginPropertiesNode removeBlockNode(Location location);
}

package com.zazsona.decorheads.blockmeta.library.node;

import org.bukkit.Location;

public interface IMutableBlockPluginPropertiesNodeParent extends IBlockPluginPropertiesNodeParent, IMutableBlockPluginPropertiesNode
{
    /**
     * Sets the {@link BlockPluginPropertiesNode}, including all its children, for the provided {@link Location}, overwriting any
     * previously set values.
     *
     * @param blockLocation the block location
     * @param blockNode the BlockNode to set
     */
    BlockPluginPropertiesNode putBlockNode(Location blockLocation, BlockPluginPropertiesNode blockNode);

    /**
     * Removes the {@link BlockPluginPropertiesNode}, including all its children, for the provided {@link Location}.
     *
     * @param blockLocation the block location
     */
    BlockPluginPropertiesNode removeBlockNode(Location blockLocation);
}

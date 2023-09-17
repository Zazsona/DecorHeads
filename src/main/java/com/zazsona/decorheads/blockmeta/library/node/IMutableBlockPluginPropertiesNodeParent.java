package com.zazsona.decorheads.blockmeta.library.node;

import org.bukkit.Location;

public interface IMutableBlockPluginPropertiesNodeParent extends IBlockPluginPropertiesNodeParent, IMutableBlockPluginPropertiesNode
{
    /**
     * Sets the {@link BlockPluginPropertiesNode}, including all its children, for the provided co-ordinates, overwriting any
     * previously set values.
     *
     * @param blockX the block's X co-ordinate
     * @param blockY the block's Y co-ordinate
     * @param blockZ the block's Z co-ordinate
     * @param blockNode the block node to set
     * @return the previously associated block node
     */
    BlockPluginPropertiesNode putBlockNode(int blockX, int blockY, int blockZ, BlockPluginPropertiesNode blockNode);

    /**
     * Removes the {@link BlockPluginPropertiesNode}, including all its children, for the provided co-ordinates
     *
     * @param blockX the block's X co-ordinate
     * @param blockY the block's Y co-ordinate
     * @param blockZ the block's Z co-ordinate
     * @return the removed block node
     */
    BlockPluginPropertiesNode removeBlockNode(int blockX, int blockY, int blockZ);
}

package com.zazsona.decorheads.blockmeta.library.node;

import org.bukkit.World;

public interface IMutableWorldBlockPluginPropertiesNodeParent extends IWorldBlockPluginPropertiesNodeParent, IMutableBlockPluginPropertiesNode
{
    /**
     * Sets the {@link WorldBlockPluginPropertiesNode}, including all its children, for the provided world,
     * overwriting any previously set values.
     *
     * @param world the world
     * @param worldNode the world node
     * @return the replaced world node, if any.
     */
    WorldBlockPluginPropertiesNode putWorldNode(World world, WorldBlockPluginPropertiesNode worldNode);

    /**
     * Removes the {@link WorldBlockPluginPropertiesNode}, including all its children, for the provided world.
     *
     * @param world the world
     * @return the removed world node
     */
    WorldBlockPluginPropertiesNode removeWorldNode(World world);
}

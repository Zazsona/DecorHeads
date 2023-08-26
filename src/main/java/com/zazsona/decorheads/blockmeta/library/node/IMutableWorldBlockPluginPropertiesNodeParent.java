package com.zazsona.decorheads.blockmeta.library.node;

import org.bukkit.Location;

public interface IMutableWorldBlockPluginPropertiesNodeParent extends IWorldBlockPluginPropertiesNodeParent, IMutableBlockPluginPropertiesNode
{
    /**
     * Sets the {@link WorldBlockPluginPropertiesNode}, including all its children, for the provided {@link Location}, overwriting any
     * previously set values.
     *
     * @param location a location in the world
     * @param worldNode the WorldNode to set
     */
    WorldBlockPluginPropertiesNode putWorldNode(Location location, WorldBlockPluginPropertiesNode worldNode);

    /**
     * Removes the {@link WorldBlockPluginPropertiesNode}, including all its children, for the provided {@link Location}.
     *
     * @param location a location in the world
     */
    WorldBlockPluginPropertiesNode removeWorldNode(Location location);
}

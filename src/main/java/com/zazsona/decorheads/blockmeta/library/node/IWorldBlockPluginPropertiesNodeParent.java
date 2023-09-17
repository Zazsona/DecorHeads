package com.zazsona.decorheads.blockmeta.library.node;

import org.bukkit.World;

import java.util.List;
import java.util.UUID;

public interface IWorldBlockPluginPropertiesNodeParent extends IBlockPluginPropertiesNode
{
    /**
     * Gets the {@link WorldBlockPluginPropertiesNode} for the world at the provided world.
     * @param world the world
     * @return the world node
     */
    WorldBlockPluginPropertiesNode getWorldNode(World world);

    /**
     * Returns a new list of contained world GUIDs
     * @return a list of contained world GUIDs
     */
    List<UUID> getWorldIds();
}

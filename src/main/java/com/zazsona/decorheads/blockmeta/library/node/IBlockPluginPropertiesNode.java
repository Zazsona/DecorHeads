package com.zazsona.decorheads.blockmeta.library.node;

import org.bukkit.Location;

public interface IBlockPluginPropertiesNode
{
    /**
     * Gets the plugin property identified by the key for the block at the given {@link Location}
     *
     * @param location the location of the block
     * @param key      the property key
     */
    String getBlockProperty(Location location, String key);
}

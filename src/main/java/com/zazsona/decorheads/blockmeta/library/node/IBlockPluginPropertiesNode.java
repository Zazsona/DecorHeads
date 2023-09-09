package com.zazsona.decorheads.blockmeta.library.node;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public interface IBlockPluginPropertiesNode
{
    /**
     * Gets the plugin property identified by the key for the block at the given {@link Location}
     *
     * @param location the location of the block
     * @param key      the property key
     */
    String getBlockProperty(Location location, String key);

    /**
     * Gets the plugin properties identified by the keys for the block at the given {@link Location}
     *
     * @param location the location of the block
     * @param keys      the property keys
     * @return a map of keys to their values. Provided keys with no value are excluded.
     */
    Map<String, String> getBlockProperties(Location location, String... keys);
}

package com.zazsona.decorheads.blockmeta.library.node;

import org.bukkit.Location;

import java.util.Map;

public interface IMutableBlockPluginPropertiesNode extends IBlockPluginPropertiesNode
{
    /**
     * Sets the plugin property identified by the key for the block at the given {@link Location}
     *
     * @param blockLocation the block location of the block
     * @param key      the property key
     * @param value    the property value
     */
    String putBlockProperty(Location blockLocation, String key, String value);

    /**
     * Sets the plugin properties identified by the key for the block at the given {@link Location}
     *
     * @param blockLocation the block location of the block
     * @param keyValueMap the properties to set
     */
    void putBlockProperties(Location blockLocation, Map<String, String> keyValueMap);

    /**
     * Removes the plugin property identified by the key for the block at the given {@link Location}
     *
     * @param blockLocation the block location of the block
     * @param key      the property key
     */
    String removeBlockProperty(Location blockLocation, String key);

    /**
     * Removes the plugin properties identified by the keys for the block at the given {@link Location}
     *
     * @param blockLocation the block location of the block
     * @param keys      the property keys
     */
    void removeBlockProperties(Location blockLocation, String... keys);
}

package com.zazsona.decorheads.blockmeta.library.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.bukkit.Location;

import java.util.*;

public class BlockPluginPropertiesNode extends Node implements IMutableBlockPluginPropertiesNode
{
    protected static final String SERIALIZED_NAME_KEY = "properties";

    @Expose
    @SerializedName(SERIALIZED_NAME_KEY)
    private HashMap<String, String> properties;

    public BlockPluginPropertiesNode()
    {
        super();
        properties = new HashMap<>();
    }

    public BlockPluginPropertiesNode(Node parent)
    {
        super(parent);
        properties = new HashMap<>();
    }

    @Override
    public String putBlockProperty(Location location, String key, String value)
    {
        return properties.put(key, value);
    }

    @Override
    public void putBlockProperties(Location location, Map<String, String> keyValueMap)
    {
        putBlockProperties(keyValueMap);
    }

    public String putBlockProperty(String key, String value)
    {
        return properties.put(key, value);
    }

    public void putBlockProperties(Map<String, String> keyValueMap)
    {
        properties.putAll(keyValueMap);
    }

    @Override
    public String removeBlockProperty(Location location, String key)
    {
        return properties.remove(key);
    }

    @Override
    public void removeBlockProperties(Location location, String... keys)
    {
        removeBlockProperties(keys);
    }

    public String removeBlockProperty(String key)
    {
        return properties.remove(key);
    }

    public void removeBlockProperties(String... keys)
    {
        for (String key : keys)
            properties.remove(key);
    }

    @Override
    public String getBlockProperty(Location location, String key)
    {
        return properties.get(key);
    }

    @Override
    public Map<String, String> getBlockProperties(Location location, String... keys)
    {
        return getBlockProperties(keys);
    }

    public String getBlockProperty(String key)
    {
        return properties.get(key);
    }

    public Map<String, String> getBlockProperties(String... keys)
    {
        HashMap<String, String> requestedProperties = new HashMap<>();
        for (String key : keys)
        {
            if (properties.containsKey(key))
                requestedProperties.put(key, properties.get(key));
        }
        return requestedProperties;
    }

    public boolean isPropertyInChildren(String key)
    {
        return properties.containsKey(key);
    }

    /**
     * Returns a new list of contained property keys
     * @return a list of contained property keys
     */
    public List<String> getPropertyKeys()
    {
        return new ArrayList<>(properties.keySet());
    }

    /**
     * Returns a new list of contained property values
     * @return a list of contained property values
     */
    public List<String> getPropertyValues()
    {
        return new ArrayList<>(properties.values());
    }

    /**
     * Returns a new list of contained property key:value pairs
     * @return a list of contained property key:value pairs
     */
    public List<Map.Entry<String, String>> getPropertyEntries()
    {
        return new ArrayList<>(properties.entrySet());
    }


}

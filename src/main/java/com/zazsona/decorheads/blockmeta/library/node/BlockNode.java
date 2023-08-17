package com.zazsona.decorheads.blockmeta.library.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.*;

public class BlockNode extends Node implements IMutableNode
{
    protected static final String SERIALIZED_NAME_KEY = "properties";

    @Expose
    @SerializedName(SERIALIZED_NAME_KEY)
    private HashMap<String, String> properties;

    public BlockNode()
    {
        super();
        properties = new HashMap<>();
    }

    public BlockNode(Node parent)
    {
        super(parent);
        properties = new HashMap<>();
    }

    @Override
    public String putBlockProperty(Location location, String key, String value)
    {
        return properties.put(key, value);
    }

    public String putBlockProperty(String key, String value)
    {
        return properties.put(key, value);
    }

    @Override
    public String removeBlockProperty(Location location, String key)
    {
        return properties.remove(key);
    }

    public String removeBlockProperty(String key)
    {
        return properties.remove(key);
    }


    @Override
    public String getBlockProperty(Location location, String key)
    {
        return properties.get(key);
    }

    public String getBlockProperty(String key)
    {
        return properties.get(key);
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

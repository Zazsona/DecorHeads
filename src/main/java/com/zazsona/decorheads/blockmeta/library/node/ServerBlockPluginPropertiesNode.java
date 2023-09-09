package com.zazsona.decorheads.blockmeta.library.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.*;

public class ServerBlockPluginPropertiesNode extends Node implements IMutableBlockPluginPropertiesNode, IMutableWorldBlockPluginPropertiesNodeParent
{
    protected static final String SERIALIZED_NAME_KEY = "worlds";

    @Expose
    @SerializedName(SERIALIZED_NAME_KEY)
    private HashMap<UUID, WorldBlockPluginPropertiesNode> children;

    public ServerBlockPluginPropertiesNode()
    {
        super();
        children = new HashMap<>();
    }

    public ServerBlockPluginPropertiesNode(Node parent)
    {
        super(parent);
        children = new HashMap<>();
    }

    @Override
    public String putBlockProperty(Location location, String key, String value)
    {
        return putBlockProperty(location.toVector(), location.getWorld(), key, value);
    }

    @Override
    public void putBlockProperties(Location location, Map<String, String> keyValueMap)
    {
        putBlockProperties(location.toVector(), location.getWorld(), keyValueMap);
    }

    public String putBlockProperty(Vector vector, World world, String key, String value)
    {
        return getWorldNode(world, true).putBlockProperty(vector, key, value);
    }

    public void putBlockProperties(Vector vector, World world, Map<String, String> keyValueMap)
    {
        getWorldNode(world, true).putBlockProperties(vector, keyValueMap);
    }

    @Override
    public String removeBlockProperty(Location location, String key)
    {
        return removeBlockProperty(location.toVector(), location.getWorld(), key);
    }

    @Override
    public void removeBlockProperties(Location location, String... keys)
    {
        removeBlockProperties(location.toVector(), location.getWorld(), keys);
    }

    public String removeBlockProperty(Vector vector, World world, String key)
    {
        WorldBlockPluginPropertiesNode worldNode = getWorldNode(world, false);
        if (worldNode != null)
            return worldNode.removeBlockProperty(vector, key);
        else
            return null;
    }

    public void removeBlockProperties(Vector vector, World world, String... keys)
    {
        WorldBlockPluginPropertiesNode worldNode = getWorldNode(world, false);
        if (worldNode != null)
            worldNode.removeBlockProperties(vector, keys);
    }

    @Override
    public String getBlockProperty(Location location, String key)
    {
        return getBlockProperty(location.toVector(), location.getWorld(), key);
    }

    @Override
    public Map<String, String> getBlockProperties(Location location, String... keys)
    {
        return getBlockProperties(location.toVector(), location.getWorld(), keys);
    }

    public String getBlockProperty(Vector vector, World world, String key)
    {
        WorldBlockPluginPropertiesNode worldNode = getWorldNode(world, false);
        if (worldNode != null)
            return worldNode.getBlockProperty(vector, key);
        else
            return null;
    }

    public Map<String, String> getBlockProperties(Vector vector, World world, String... keys)
    {
        WorldBlockPluginPropertiesNode worldNode = getWorldNode(world, false);
        if (worldNode != null)
            return worldNode.getBlockProperties(vector, keys);
        else
            return new HashMap<>();
    }

    @Override
    public WorldBlockPluginPropertiesNode putWorldNode(Location location, WorldBlockPluginPropertiesNode worldNode)
    {
        if (worldNode == null)
            throw new NullArgumentException("worldNode");

        UUID key = getWorldKey(location);
        return children.put(key, worldNode);
    }

    /**
     * Sets the {@link WorldBlockPluginPropertiesNode}, including all its children, for the provided {@link World}, overwriting any
     * previously set values.
     *
     * @param world the world
     * @param worldNode the WorldNode to set
     */
    public WorldBlockPluginPropertiesNode putWorldNode(World world, WorldBlockPluginPropertiesNode worldNode)
    {
        if (worldNode == null)
            throw new NullArgumentException("worldNode");

        UUID key = getWorldKey(world);
        return children.put(key, worldNode);
    }

    @Override
    public WorldBlockPluginPropertiesNode removeWorldNode(Location location)
    {
        UUID key = getWorldKey(location);
        return children.remove(key);
    }

    /**
     * Removes the {@link WorldBlockPluginPropertiesNode}, including all its children, for the provided {@link World}.
     *
     * @param world the world
     */
    public WorldBlockPluginPropertiesNode removeWorldNode(World world)
    {
        UUID key = getWorldKey(world);
        return children.remove(key);
    }

    public boolean isWorldInChildren(Location location)
    {
        return isWorldInChildren(location.getWorld());
    }
    public boolean isWorldInChildren(World world)
    {
        return getWorldNode(world, false) != null;
    }

    /**
     * Returns a new list of contained world GUID keys
     * @return a list of contained world GUID keys
     */
    public List<UUID> getWorldKeys()
    {
        return new ArrayList<>(children.keySet());
    }

    /**
     * Returns a new list of contained {@link WorldBlockPluginPropertiesNode} values
     * @return a list of contained {@link WorldBlockPluginPropertiesNode} values
     */
    public List<WorldBlockPluginPropertiesNode> getWorldValues()
    {
        return new ArrayList<>(children.values());
    }

    /**
     * Returns a new list of {@link WorldBlockPluginPropertiesNode} key:value pairs
     * @return a list of property key:value pairs, with the key a world GUID,
     * and the value a {@link WorldBlockPluginPropertiesNode}
     */
    public List<Map.Entry<UUID, WorldBlockPluginPropertiesNode>> getWorldEntries()
    {
        return new ArrayList<>(children.entrySet());
    }

    public WorldBlockPluginPropertiesNode getWorldNode(Location location)
    {
        if (location == null)
            throw new NullArgumentException("location");

        return getWorldNode(location.getWorld(), false);
    }

    /**
     * Gets the {@link WorldBlockPluginPropertiesNode} for the world.
     * @param world the world
     * @return the WorldNode
     */
    public WorldBlockPluginPropertiesNode getWorldNode(World world)
    {
        if (world == null)
            throw new NullArgumentException("world");

        return getWorldNode(world, false);
    }

    private WorldBlockPluginPropertiesNode getWorldNode(World world, boolean createIfNotExists)
    {
        UUID key = getWorldKey(world);
        if (!children.containsKey(key) && createIfNotExists)
            children.put(key, new WorldBlockPluginPropertiesNode(this));

        return children.get(key);
    }

    private UUID getWorldKey(Location location)
    {
        if (location == null)
            throw new NullArgumentException("location");

        return getWorldKey(location.getWorld());
    }

    private UUID getWorldKey(World world)
    {
        if (world == null)
            throw new NullArgumentException("world");

        return world.getUID();
    }

}

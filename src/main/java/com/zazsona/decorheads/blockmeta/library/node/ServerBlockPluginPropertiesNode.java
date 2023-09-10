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

    ServerBlockPluginPropertiesNode(Node parent)
    {
        super(parent);
        children = new HashMap<>();
    }

    @Override
    public String putBlockProperty(Location blockLocation, String key, String value)
    {
        return putBlockProperty(blockLocation.toVector(), blockLocation.getWorld(), key, value);
    }

    @Override
    public void putBlockProperties(Location blockLocation, Map<String, String> keyValueMap)
    {
        putBlockProperties(blockLocation.toVector(), blockLocation.getWorld(), keyValueMap);
    }

    public String putBlockProperty(Vector blockVector, World world, String key, String value)
    {
        return getWorldNode(world, true).putBlockProperty(blockVector, key, value);
    }

    public void putBlockProperties(Vector blockVector, World world, Map<String, String> keyValueMap)
    {
        getWorldNode(world, true).putBlockProperties(blockVector, keyValueMap);
    }

    @Override
    public String removeBlockProperty(Location blockLocation, String key)
    {
        return removeBlockProperty(blockLocation.toVector(), blockLocation.getWorld(), key);
    }

    @Override
    public void removeBlockProperties(Location blockLocation, String... keys)
    {
        removeBlockProperties(blockLocation.toVector(), blockLocation.getWorld(), keys);
    }

    public String removeBlockProperty(Vector blockVector, World world, String key)
    {
        WorldBlockPluginPropertiesNode worldNode = getWorldNode(world, false);
        if (worldNode != null)
            return worldNode.removeBlockProperty(blockVector, key);
        else
            return null;
    }

    public void removeBlockProperties(Vector blockVector, World world, String... keys)
    {
        WorldBlockPluginPropertiesNode worldNode = getWorldNode(world, false);
        if (worldNode != null)
            worldNode.removeBlockProperties(blockVector, keys);
    }

    @Override
    public String getBlockProperty(Location blockLocation, String key)
    {
        return getBlockProperty(blockLocation.toVector(), blockLocation.getWorld(), key);
    }

    @Override
    public Map<String, String> getBlockProperties(Location blockLocation, String... keys)
    {
        return getBlockProperties(blockLocation.toVector(), blockLocation.getWorld(), keys);
    }

    public String getBlockProperty(Vector blockVector, World world, String key)
    {
        WorldBlockPluginPropertiesNode worldNode = getWorldNode(world, false);
        if (worldNode != null)
            return worldNode.getBlockProperty(blockVector, key);
        else
            return null;
    }

    public Map<String, String> getBlockProperties(Vector blockVector, World world, String... keys)
    {
        WorldBlockPluginPropertiesNode worldNode = getWorldNode(world, false);
        if (worldNode != null)
            return worldNode.getBlockProperties(blockVector, keys);
        else
            return new HashMap<>();
    }

    @Override
    public WorldBlockPluginPropertiesNode putWorldNode(Location blockLocation, WorldBlockPluginPropertiesNode worldNode)
    {
        return putWorldNode(blockLocation.getWorld(), worldNode);
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
        worldNode.setParent(this);
        return children.put(key, worldNode);
    }

    @Override
    public WorldBlockPluginPropertiesNode removeWorldNode(Location blockLocation)
    {
        return removeWorldNode(blockLocation.getWorld());
    }

    /**
     * Removes the {@link WorldBlockPluginPropertiesNode}, including all its children, for the provided {@link World}.
     *
     * @param world the world
     */
    public WorldBlockPluginPropertiesNode removeWorldNode(World world)
    {
        UUID key = getWorldKey(world);
        WorldBlockPluginPropertiesNode worldNode = children.remove(key);
        if (worldNode != null)
            worldNode.setParent(null);
        return worldNode;
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

    public WorldBlockPluginPropertiesNode getWorldNode(Location blockLocation)
    {
        if (blockLocation == null)
            throw new NullArgumentException("location");

        return getWorldNode(blockLocation.getWorld(), false);
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

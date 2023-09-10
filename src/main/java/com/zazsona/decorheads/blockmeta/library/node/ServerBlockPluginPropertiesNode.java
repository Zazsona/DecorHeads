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
    public WorldBlockPluginPropertiesNode putWorldNode(World world, WorldBlockPluginPropertiesNode worldNode)
    {
        if (worldNode == null)
            throw new NullArgumentException("worldNode");

        UUID key = getWorldId(world);
        worldNode.setParent(this);
        return children.put(key, worldNode);
    }

    public WorldBlockPluginPropertiesNode putWorldNode(Location blockLocation, WorldBlockPluginPropertiesNode worldNode)
    {
        return putWorldNode(blockLocation.getWorld(), worldNode);
    }

    @Override
    public WorldBlockPluginPropertiesNode removeWorldNode(World world)
    {
        UUID key = getWorldId(world);
        WorldBlockPluginPropertiesNode worldNode = children.remove(key);
        if (worldNode != null)
            worldNode.setParent(null);
        return worldNode;
    }

    public WorldBlockPluginPropertiesNode removeWorldNode(Location blockLocation)
    {
        return removeWorldNode(blockLocation.getWorld());
    }

    public WorldBlockPluginPropertiesNode getWorldNode(Location blockLocation)
    {
        if (blockLocation == null)
            throw new NullArgumentException("location");

        return getWorldNode(blockLocation.getWorld());
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
        UUID key = getWorldId(world);
        if (!children.containsKey(key) && createIfNotExists)
            children.put(key, new WorldBlockPluginPropertiesNode(this));

        return children.get(key);
    }

    public boolean isWorldInChildren(Location blockLocation)
    {
        return isWorldInChildren(blockLocation.getWorld());
    }
    public boolean isWorldInChildren(World world)
    {
        return getWorldNode(world) != null;
    }

    /**
     * Returns a new list of contained world GUIDs
     * @return a list of contained world GUID keys
     */
    public List<UUID> getWorldIds()
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

    private UUID getWorldId(Location blockLocation)
    {
        if (blockLocation == null)
            throw new NullArgumentException("blockLocation");

        return getWorldId(blockLocation.getWorld());
    }

    private UUID getWorldId(World world)
    {
        if (world == null)
            throw new NullArgumentException("world");

        return world.getUID();
    }

}

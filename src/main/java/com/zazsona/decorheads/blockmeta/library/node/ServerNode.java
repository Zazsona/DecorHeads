package com.zazsona.decorheads.blockmeta.library.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.*;

public class ServerNode extends Node implements IMutableNode, IMutableWorldNodeParent
{
    protected static final String SERIALIZED_NAME_KEY = "worlds";

    @Expose
    @SerializedName(SERIALIZED_NAME_KEY)
    private HashMap<UUID, WorldNode> children;

    public ServerNode()
    {
        super();
        children = new HashMap<>();
    }

    public ServerNode(Node parent)
    {
        super(parent);
        children = new HashMap<>();
    }

    @Override
    public String putBlockProperty(Location location, String key, String value)
    {
        return putBlockProperty(location.toVector(), location.getWorld(), key, value);
    }

    public String putBlockProperty(Vector vector, World world, String key, String value)
    {
        return getWorldNode(world, true).putBlockProperty(vector, key, value);
    }

    @Override
    public String removeBlockProperty(Location location, String key)
    {
        return removeBlockProperty(location.toVector(), location.getWorld(), key);
    }

    public String removeBlockProperty(Vector vector, World world, String key)
    {
        WorldNode worldNode = getWorldNode(world, false);
        if (worldNode != null)
            return worldNode.removeBlockProperty(vector, key);
        else
            return null;
    }

    @Override
    public String getBlockProperty(Location location, String key)
    {
        return getBlockProperty(location.toVector(), location.getWorld(), key);
    }

    public String getBlockProperty(Vector vector, World world, String key)
    {
        WorldNode worldNode = getWorldNode(world, false);
        if (worldNode != null)
            return worldNode.getBlockProperty(vector, key);
        else
            return null;
    }

    @Override
    public WorldNode putWorldNode(Location location, WorldNode worldNode)
    {
        if (worldNode == null)
            throw new NullArgumentException("worldNode");

        UUID key = getWorldKey(location);
        return children.put(key, worldNode);
    }

    /**
     * Sets the {@link WorldNode}, including all its children, for the provided {@link World}, overwriting any
     * previously set values.
     *
     * @param world the world
     * @param worldNode the WorldNode to set
     */
    public WorldNode putWorldNode(World world, WorldNode worldNode)
    {
        if (worldNode == null)
            throw new NullArgumentException("worldNode");

        UUID key = getWorldKey(world);
        return children.put(key, worldNode);
    }

    @Override
    public WorldNode removeWorldNode(Location location)
    {
        UUID key = getWorldKey(location);
        return children.remove(key);
    }

    /**
     * Removes the {@link WorldNode}, including all its children, for the provided {@link World}.
     *
     * @param world the world
     */
    public WorldNode removeWorldNode(World world)
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
     * Returns a new list of contained {@link WorldNode} values
     * @return a list of contained {@link WorldNode} values
     */
    public List<WorldNode> getWorldValues()
    {
        return new ArrayList<>(children.values());
    }

    /**
     * Returns a new list of {@link WorldNode} key:value pairs
     * @return a list of property key:value pairs, with the key a world GUID,
     * and the value a {@link WorldNode}
     */
    public List<Map.Entry<UUID, WorldNode>> getWorldEntries()
    {
        return new ArrayList<>(children.entrySet());
    }

    public WorldNode getWorldNode(Location location)
    {
        if (location == null)
            throw new NullArgumentException("location");

        return getWorldNode(location.getWorld(), false);
    }

    /**
     * Gets the {@link WorldNode} for the world.
     * @param world the world
     * @return the WorldNode
     */
    public WorldNode getWorldNode(World world)
    {
        if (world == null)
            throw new NullArgumentException("world");

        return getWorldNode(world, false);
    }

    private WorldNode getWorldNode(World world, boolean createIfNotExists)
    {
        UUID key = getWorldKey(world);
        if (!children.containsKey(key) && createIfNotExists)
            children.put(key, new WorldNode(this));

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

package com.zazsona.decorheads.blockmeta.library.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegionBlockPluginPropertiesNode extends Node implements IMutableBlockPluginPropertiesNode, IMutableChunkBlockPluginPropertiesNodeParent
{
    protected static final String SERIALIZED_NAME_KEY = "chunks";

    @Expose
    @SerializedName(SERIALIZED_NAME_KEY)
    private HashMap<Vector, ChunkBlockPluginPropertiesNode> children;

    public RegionBlockPluginPropertiesNode()
    {
        super();
        children = new HashMap<>();
    }

    public RegionBlockPluginPropertiesNode(Node parent)
    {
        super(parent);
        children = new HashMap<>();
    }

    @Override
    public String putBlockProperty(Location location, String key, String value)
    {
        return putBlockProperty(location.toVector(), key, value);
    }

    @Override
    public void putBlockProperties(Location location, Map<String, String> keyValueMap)
    {
        putBlockProperties(location.toVector(), keyValueMap);
    }

    public String putBlockProperty(Vector vector, String key, String value)
    {
        return getChunkNode(vector, true).putBlockProperty(vector, key, value);
    }

    public void putBlockProperties(Vector vector, Map<String, String> keyValueMap)
    {
        getChunkNode(vector, true).putBlockProperties(vector, keyValueMap);
    }

    @Override
    public String removeBlockProperty(Location location, String key)
    {
        return removeBlockProperty(location.toVector(), key);
    }

    @Override
    public void removeBlockProperties(Location location, String... keys)
    {
        removeBlockProperties(location.toVector(), keys);
    }

    public String removeBlockProperty(Vector vector, String key)
    {
        ChunkBlockPluginPropertiesNode chunkNode = getChunkNode(vector, false);
        if (chunkNode != null)
            return chunkNode.removeBlockProperty(vector, key);
        else
            return null;
    }

    public void removeBlockProperties(Vector vector, String... keys)
    {
        ChunkBlockPluginPropertiesNode chunkNode = getChunkNode(vector, false);
        if (chunkNode != null)
            chunkNode.removeBlockProperties(vector, keys);
    }

    @Override
    public String getBlockProperty(Location location, String key)
    {
        return getBlockProperty(location.toVector(), key);
    }

    @Override
    public Map<String, String> getBlockProperties(Location location, String... keys)
    {
        return getBlockProperties(location.toVector(), keys);
    }

    public String getBlockProperty(Vector vector, String key)
    {
        ChunkBlockPluginPropertiesNode chunkNode = getChunkNode(vector, false);
        if (chunkNode != null)
            return chunkNode.getBlockProperty(vector, key);
        else
            return null;
    }

    public Map<String, String> getBlockProperties(Vector vector, String... keys)
    {
        ChunkBlockPluginPropertiesNode chunkNode = getChunkNode(vector, false);
        if (chunkNode != null)
            return chunkNode.getBlockProperties(vector, keys);
        else
            return new HashMap<>();
    }

    @Override
    public ChunkBlockPluginPropertiesNode putChunkNode(Location location, ChunkBlockPluginPropertiesNode chunkNode)
    {
        if (location == null)
            throw new NullArgumentException("location");

        return putChunkNode(location.toVector(), chunkNode);
    }

    public ChunkBlockPluginPropertiesNode putChunkNode(Vector vector, ChunkBlockPluginPropertiesNode chunkNode)
    {
        if (vector == null)
            throw new NullArgumentException("vector");
        if (chunkNode == null)
            throw new NullArgumentException("chunkNode");

        Vector key = getChunkKey(vector);
        return children.put(key, chunkNode);
    }

    @Override
    public ChunkBlockPluginPropertiesNode removeChunkNode(Location location)
    {
        return removeChunkNode(location.toVector());
    }

    public ChunkBlockPluginPropertiesNode removeChunkNode(Vector vector)
    {
        Vector key = getChunkKey(vector);
        return children.remove(key);
    }

    public boolean isChunkInChildren(Location location)
    {
        return isChunkInChildren(location.toVector());
    }

    public boolean isChunkInChildren(Vector vector)
    {
        return getChunkNode(vector, false) != null;
    }

    /**
     * Returns a new list of contained chunk location keys
     * @return a list of contained chunk location keys
     */
    public List<Vector> getChunkKeys()
    {
        return new ArrayList<>(children.keySet());
    }

    /**
     * Returns a new list of contained {@link ChunkBlockPluginPropertiesNode} values
     * @return a list of contained {@link ChunkBlockPluginPropertiesNode} values
     */
    public List<ChunkBlockPluginPropertiesNode> getChunkValues()
    {
        return new ArrayList<>(children.values());
    }

    /**
     * Returns a new list of {@link ChunkBlockPluginPropertiesNode} key:value pairs
     * @return a list of property key:value pairs, with the key a representation of chunk location,
     * and the value a {@link ChunkBlockPluginPropertiesNode}
     */
    public List<Map.Entry<Vector, ChunkBlockPluginPropertiesNode>> getChunkEntries()
    {
        return new ArrayList<>(children.entrySet());
    }

    public ChunkBlockPluginPropertiesNode getChunkNode(Location location)
    {
        return getChunkNode(location.toVector());
    }
    public ChunkBlockPluginPropertiesNode getChunkNode(Vector vector)
    {
        return getChunkNode(vector, false);
    }

    private ChunkBlockPluginPropertiesNode getChunkNode(Vector vector, boolean createIfNotExists)
    {
        Vector key = getChunkKey(vector);
        if (!children.containsKey(key) && createIfNotExists)
            children.put(key, new ChunkBlockPluginPropertiesNode(this));

        return children.get(key);
    }

    private Vector getChunkKey(Vector vector)
    {
        int chunkX = (int) Math.floor(vector.getBlockX() / 16);
        int chunkZ = (int) Math.floor(vector.getBlockZ() / 16);

        return new Vector(chunkX, 0, chunkZ);
    }
}

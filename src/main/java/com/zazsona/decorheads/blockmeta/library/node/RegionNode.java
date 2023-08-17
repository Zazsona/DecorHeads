package com.zazsona.decorheads.blockmeta.library.node;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegionNode extends Node implements IMutableNode, IMutableChunkNodeParent
{
    protected static final String SERIALIZED_NAME_KEY = "chunks";

    @Expose
    @SerializedName(SERIALIZED_NAME_KEY)
    private HashMap<Vector, ChunkNode> children;

    public RegionNode()
    {
        super();
        children = new HashMap<>();
    }

    public RegionNode(Node parent)
    {
        super(parent);
        children = new HashMap<>();
    }

    @Override
    public String putBlockProperty(Location location, String key, String value)
    {
        return putBlockProperty(location.toVector(), key, value);
    }

    public String putBlockProperty(Vector vector, String key, String value)
    {
        return getChunkNode(vector, true).putBlockProperty(vector, key, value);
    }

    @Override
    public String removeBlockProperty(Location location, String key)
    {
        return removeBlockProperty(location.toVector(), key);
    }

    public String removeBlockProperty(Vector vector, String key)
    {
        ChunkNode chunkNode = getChunkNode(vector, false);
        if (chunkNode != null)
            return chunkNode.removeBlockProperty(vector, key);
        else
            return null;
    }

    @Override
    public String getBlockProperty(Location location, String key)
    {
        return getBlockProperty(location.toVector(), key);
    }

    public String getBlockProperty(Vector vector, String key)
    {
        ChunkNode chunkNode = getChunkNode(vector, false);
        if (chunkNode != null)
            return chunkNode.getBlockProperty(vector, key);
        else
            return null;
    }

    @Override
    public ChunkNode putChunkNode(Location location, ChunkNode chunkNode)
    {
        if (location == null)
            throw new NullArgumentException("location");

        return putChunkNode(location.toVector(), chunkNode);
    }

    public ChunkNode putChunkNode(Vector vector, ChunkNode chunkNode)
    {
        if (vector == null)
            throw new NullArgumentException("vector");
        if (chunkNode == null)
            throw new NullArgumentException("chunkNode");

        Vector key = getChunkKey(vector);
        return children.put(key, chunkNode);
    }

    @Override
    public ChunkNode removeChunkNode(Location location)
    {
        return removeChunkNode(location.toVector());
    }

    public ChunkNode removeChunkNode(Vector vector)
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
     * Returns a new list of contained {@link ChunkNode} values
     * @return a list of contained {@link ChunkNode} values
     */
    public List<ChunkNode> getChunkValues()
    {
        return new ArrayList<>(children.values());
    }

    /**
     * Returns a new list of {@link ChunkNode} key:value pairs
     * @return a list of property key:value pairs, with the key a representation of chunk location,
     * and the value a {@link ChunkNode}
     */
    public List<Map.Entry<Vector, ChunkNode>> getChunkEntries()
    {
        return new ArrayList<>(children.entrySet());
    }

    public ChunkNode getChunkNode(Location location)
    {
        return getChunkNode(location.toVector());
    }
    public ChunkNode getChunkNode(Vector vector)
    {
        return getChunkNode(vector, false);
    }

    private ChunkNode getChunkNode(Vector vector, boolean createIfNotExists)
    {
        Vector key = getChunkKey(vector);
        if (!children.containsKey(key) && createIfNotExists)
            children.put(key, new ChunkNode(this));

        return children.get(key);
    }

    private Vector getChunkKey(Vector vector)
    {
        int chunkX = (int) Math.floor(vector.getBlockX() / 16);
        int chunkZ = (int) Math.floor(vector.getBlockZ() / 16);

        return new Vector(chunkX, 0, chunkZ);
    }
}

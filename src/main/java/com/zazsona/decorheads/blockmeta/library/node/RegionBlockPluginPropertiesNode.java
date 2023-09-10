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

    RegionBlockPluginPropertiesNode(Node parent)
    {
        super(parent);
        children = new HashMap<>();
    }

    @Override
    public String putBlockProperty(Location blockLocation, String key, String value)
    {
        return putBlockProperty(blockLocation.toVector(), key, value);
    }

    @Override
    public void putBlockProperties(Location blockLocation, Map<String, String> keyValueMap)
    {
        putBlockProperties(blockLocation.toVector(), keyValueMap);
    }

    public String putBlockProperty(Vector blockVector, String key, String value)
    {
        return getChunkNode(blockVector, true).putBlockProperty(blockVector, key, value);
    }

    public void putBlockProperties(Vector blockVector, Map<String, String> keyValueMap)
    {
        getChunkNode(blockVector, true).putBlockProperties(blockVector, keyValueMap);
    }

    @Override
    public String removeBlockProperty(Location blockLocation, String key)
    {
        return removeBlockProperty(blockLocation.toVector(), key);
    }

    @Override
    public void removeBlockProperties(Location blockLocation, String... keys)
    {
        removeBlockProperties(blockLocation.toVector(), keys);
    }

    public String removeBlockProperty(Vector blockVector, String key)
    {
        ChunkBlockPluginPropertiesNode chunkNode = getChunkNode(blockVector, false);
        if (chunkNode != null)
            return chunkNode.removeBlockProperty(blockVector, key);
        else
            return null;
    }

    public void removeBlockProperties(Vector blockVector, String... keys)
    {
        ChunkBlockPluginPropertiesNode chunkNode = getChunkNode(blockVector, false);
        if (chunkNode != null)
            chunkNode.removeBlockProperties(blockVector, keys);
    }

    @Override
    public String getBlockProperty(Location blockLocation, String key)
    {
        return getBlockProperty(blockLocation.toVector(), key);
    }

    @Override
    public Map<String, String> getBlockProperties(Location blockLocation, String... keys)
    {
        return getBlockProperties(blockLocation.toVector(), keys);
    }

    public String getBlockProperty(Vector blockVector, String key)
    {
        ChunkBlockPluginPropertiesNode chunkNode = getChunkNode(blockVector, false);
        if (chunkNode != null)
            return chunkNode.getBlockProperty(blockVector, key);
        else
            return null;
    }

    public Map<String, String> getBlockProperties(Vector blockVector, String... keys)
    {
        ChunkBlockPluginPropertiesNode chunkNode = getChunkNode(blockVector, false);
        if (chunkNode != null)
            return chunkNode.getBlockProperties(blockVector, keys);
        else
            return new HashMap<>();
    }

    @Override
    public ChunkBlockPluginPropertiesNode putChunkNode(int chunkX, int chunkZ, ChunkBlockPluginPropertiesNode chunkNode)
    {
        return putChunkNode(getChunkVector(chunkX, chunkZ), chunkNode);
    }

    public ChunkBlockPluginPropertiesNode putChunkNode(Location blockLocation, ChunkBlockPluginPropertiesNode chunkNode)
    {
        if (blockLocation == null)
            throw new NullArgumentException("location");

        return putChunkNode(getChunkVector(blockLocation.toVector()), chunkNode);
    }

    private ChunkBlockPluginPropertiesNode putChunkNode(Vector chunkVector, ChunkBlockPluginPropertiesNode chunkNode)
    {
        if (chunkVector == null)
            throw new NullArgumentException("chunkVector");
        if (chunkNode == null)
            throw new NullArgumentException("chunkNode");

        chunkNode.setParent(this);
        return children.put(chunkVector, chunkNode);
    }

    @Override
    public ChunkBlockPluginPropertiesNode removeChunkNode(int chunkX, int chunkZ)
    {
        return removeChunkNode(getChunkVector(chunkX, chunkZ));
    }

    public ChunkBlockPluginPropertiesNode removeChunkNode(Location blockLocation)
    {
        return removeChunkNode(getChunkVector(blockLocation.toVector()));
    }

    private ChunkBlockPluginPropertiesNode removeChunkNode(Vector chunkVector)
    {
        ChunkBlockPluginPropertiesNode chunkNode = children.remove(chunkVector);
        if (chunkNode != null)
            chunkNode.setParent(null);
        return chunkNode;
    }

    @Override
    public ChunkBlockPluginPropertiesNode getChunkNode(int chunkX, int chunkZ)
    {
        return getChunkNode(getChunkVector(chunkX, chunkZ));
    }

    public ChunkBlockPluginPropertiesNode getChunkNode(Location blockLocation)
    {
        return getChunkNode(getChunkVector(blockLocation.toVector()));
    }

    private ChunkBlockPluginPropertiesNode getChunkNode(Vector chunkVector)
    {
        return getChunkNode(chunkVector, false);
    }

    private ChunkBlockPluginPropertiesNode getChunkNode(Vector chunkVector, boolean createIfNotExists)
    {
        if (!children.containsKey(chunkVector) && createIfNotExists)
            children.put(chunkVector, new ChunkBlockPluginPropertiesNode(this));

        return children.get(chunkVector);
    }

    public boolean isChunkInChildren(int chunkX, int chunkZ)
    {
        return isChunkInChildren(getChunkVector(chunkX, chunkZ));
    }

    public boolean isChunkInChildren(Location blockLocation)
    {
        return isChunkInChildren(getChunkVector(blockLocation.toVector()));
    }

    private boolean isChunkInChildren(Vector chunkVector)
    {
        return getChunkNode(chunkVector) != null;
    }

    /**
     * Returns a new list of contained chunk co-ordinates
     * @return a list of contained chunk co-ordinate keys
     */
    public List<Vector> getChunkVectors()
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

    private Vector getChunkVector(Vector blockVector)
    {
        int chunkX = (int) Math.floor(blockVector.getBlockX() / 16);
        int chunkZ = (int) Math.floor(blockVector.getBlockZ() / 16);

        return getChunkVector(chunkX, chunkZ);
    }

    private Vector getChunkVector(int chunkX, int chunkZ)
    {
        return new Vector(chunkX, 0, chunkZ);
    }
}

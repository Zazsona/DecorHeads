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

public class ChunkBlockPluginPropertiesNode extends Node implements IMutableBlockPluginPropertiesNode, IMutableBlockPluginPropertiesNodeParent
{
    protected static final String SERIALIZED_NAME_KEY = "blocks";

    @Expose
    @SerializedName(SERIALIZED_NAME_KEY)
    private HashMap<Vector, BlockPluginPropertiesNode> children;

    public ChunkBlockPluginPropertiesNode()
    {
        super();
        children = new HashMap<>();
    }

    ChunkBlockPluginPropertiesNode(Node parent)
    {
        super(parent);
        children = new HashMap<>();
    }

    public String putBlockProperty(int blockX, int blockY, int blockZ, String key, String value)
    {
        return putBlockProperty(getBlockVector(blockX, blockY, blockZ), key, value);
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
        return getBlockNode(blockVector, true).putBlockProperty(key, value);
    }

    public void putBlockProperties(int blockX, int blockY, int blockZ, Map<String, String> keyValueMap)
    {
        putBlockProperties(getBlockVector(blockX, blockY, blockZ), keyValueMap);
    }

    public void putBlockProperties(Vector blockVector, Map<String, String> keyValueMap)
    {
        getBlockNode(blockVector, true).putBlockProperties(keyValueMap);
    }

    public String removeBlockProperty(int blockX, int blockY, int blockZ, String key)
    {
        return removeBlockProperty(getBlockVector(blockX, blockY, blockZ), key);
    }

    @Override
    public String removeBlockProperty(Location blockLocation, String key)
    {
        return removeBlockProperty(blockLocation.toVector(), key);
    }

    public void removeBlockProperties(int blockX, int blockY, int blockZ, String... keys)
    {
        removeBlockProperties(getBlockVector(blockX, blockY, blockZ), keys);
    }

    @Override
    public void removeBlockProperties(Location blockLocation, String... keys)
    {
        removeBlockProperties(blockLocation.toVector(), keys);
    }

    public String removeBlockProperty(Vector blockVector, String key)
    {
        BlockPluginPropertiesNode blockNode = getBlockNode(blockVector, false);
        if (blockNode != null)
            return blockNode.removeBlockProperty(key);
        else
            return null;
    }

    public void removeBlockProperties(Vector blockVector, String... keys)
    {
        BlockPluginPropertiesNode blockNode = getBlockNode(blockVector, false);
        if (blockNode != null)
            blockNode.removeBlockProperties(keys);
    }

    public String getBlockProperty(int blockX, int blockY, int blockZ, String key)
    {
        return getBlockProperty(getBlockVector(blockX, blockY, blockZ), key);
    }

    @Override
    public String getBlockProperty(Location blockLocation, String key)
    {
        return getBlockProperty(blockLocation.toVector(), key);
    }

    public Map<String, String> getBlockProperties(int blockX, int blockY, int blockZ, String... keys)
    {
        return getBlockProperties(getBlockVector(blockX, blockY, blockZ), keys);
    }

    @Override
    public Map<String, String> getBlockProperties(Location blockLocation, String... keys)
    {
        return getBlockProperties(blockLocation.toVector(), keys);
    }

    public String getBlockProperty(Vector blockVector, String key)
    {
        BlockPluginPropertiesNode blockNode = getBlockNode(blockVector, false);
        if (blockNode != null)
            return blockNode.getBlockProperty(key);
        else
            return null;
    }

    public Map<String, String> getBlockProperties(Vector blockVector, String... keys)
    {
        BlockPluginPropertiesNode blockNode = getBlockNode(blockVector, false);
        if (blockNode != null)
            return blockNode.getBlockProperties(keys);
        else
            return new HashMap<>();
    }

    @Override
    public BlockPluginPropertiesNode putBlockNode(int blockX, int blockY, int blockZ, BlockPluginPropertiesNode blockNode)
    {
        return putBlockNode(getBlockVector(blockX, blockY, blockZ), blockNode);
    }

    public BlockPluginPropertiesNode putBlockNode(Location blockLocation, BlockPluginPropertiesNode blockNode)
    {
        if (blockLocation == null)
            throw new NullArgumentException("location");

        return putBlockNode(blockLocation.toVector(), blockNode);
    }

    public BlockPluginPropertiesNode putBlockNode(Vector blockVector, BlockPluginPropertiesNode blockNode)
    {
        if (blockVector == null)
            throw new NullArgumentException("blockVector");
        if (blockNode == null)
            throw new NullArgumentException("blockNode");

        Vector key = getBlockVector(blockVector);
        blockNode.setParent(this);
        return children.put(key, blockNode);
    }

    @Override
    public BlockPluginPropertiesNode removeBlockNode(int blockX, int blockY, int blockZ)
    {
        return removeBlockNode(getBlockVector(blockX, blockY, blockZ));
    }

    public BlockPluginPropertiesNode removeBlockNode(Location blockLocation)
    {
        return removeBlockNode(blockLocation.toVector());
    }

    public BlockPluginPropertiesNode removeBlockNode(Vector blockVector)
    {
        Vector key = getBlockVector(blockVector);
        BlockPluginPropertiesNode blockNode = children.remove(key);
        if (blockNode != null)
            blockNode.setParent(null);
        return blockNode;
    }

    @Override
    public BlockPluginPropertiesNode getBlockNode(int blockX, int blockY, int blockZ)
    {
        return getBlockNode(getBlockVector(blockX, blockY, blockZ));
    }

    public BlockPluginPropertiesNode getBlockNode(Location blockLocation)
    {
        return getBlockNode(blockLocation.toVector());
    }
    public BlockPluginPropertiesNode getBlockNode(Vector blockVector)
    {
        return getBlockNode(blockVector, false);
    }

    public boolean isBlockInChildren(Location location)
    {
        return isBlockInChildren(location.toVector());
    }

    public boolean isBlockInChildren(int blockX, int blockY, int blockZ)
    {
        return isBlockInChildren(getBlockVector(blockX, blockY, blockZ));
    }
    public boolean isBlockInChildren(Vector blockVector)
    {
        return getBlockNode(blockVector, false) != null;
    }

    /**
     * Returns a new list of contained block co-ordinates
     * @return a list of contained block co-ordinates keys
     */
    @Override
    public List<Vector> getBlockVectors()
    {
        return new ArrayList<>(children.keySet());
    }

    /**
     * Returns a new list of contained {@link BlockPluginPropertiesNode} values
     * @return a list of contained {@link BlockPluginPropertiesNode} values
     */
    public List<BlockPluginPropertiesNode> getBlockValues()
    {
        return new ArrayList<>(children.values());
    }

    /**
     * Returns a new list of {@link BlockPluginPropertiesNode} key:value pairs
     * @return a list of property key:value pairs, with the key a representation of block location,
     * and the value a {@link BlockPluginPropertiesNode}
     */
    public List<Map.Entry<Vector, BlockPluginPropertiesNode>> getBlockEntries()
    {
        return new ArrayList<>(children.entrySet());
    }

    private BlockPluginPropertiesNode getBlockNode(Vector blockVector, boolean createIfNotExists)
    {
        Vector key = getBlockVector(blockVector);
        if (!children.containsKey(key) && createIfNotExists)
            children.put(key, new BlockPluginPropertiesNode(this));

        return children.get(key);
    }

    private Vector getBlockVector(int blockX, int blockY, int blockZ)
    {
        return new Vector(blockX, blockY, blockZ);
    }

    private Vector getBlockVector(Vector blockVector)
    {
        return blockVector.clone(); // Clone for use as a key
    }
}

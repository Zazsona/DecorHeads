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

    public void putBlockProperties(Vector blockVector, Map<String, String> keyValueMap)
    {
        getBlockNode(blockVector, true).putBlockProperties(keyValueMap);
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

        Vector key = getBlockKey(blockVector);
        blockNode.setParent(this);
        return children.put(key, blockNode);
    }

    @Override
    public BlockPluginPropertiesNode removeBlockNode(Location blockLocation)
    {
        return removeBlockNode(blockLocation.toVector());
    }

    public BlockPluginPropertiesNode removeBlockNode(Vector blockVector)
    {
        Vector key = getBlockKey(blockVector);
        BlockPluginPropertiesNode blockNode = children.remove(key);
        if (blockNode != null)
            blockNode.setParent(null);
        return blockNode;
    }

    @Override
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
    public boolean isBlockInChildren(Vector blockVector)
    {
        return getBlockNode(blockVector, false) != null;
    }

    /**
     * Returns a new list of contained block location keys
     * @return a list of contained block location keys
     */
    public List<Vector> getBlockKeys()
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
        Vector key = getBlockKey(blockVector);
        if (!children.containsKey(key) && createIfNotExists)
            children.put(key, new BlockPluginPropertiesNode(this));

        return children.get(key);
    }

    private Vector getBlockKey(Vector blockVector)
    {
        return blockVector.clone();
    }
}

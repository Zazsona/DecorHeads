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

    public ChunkBlockPluginPropertiesNode(Node parent)
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
        return getBlockNode(vector, true).putBlockProperty(key, value);
    }

    public void putBlockProperties(Vector vector, Map<String, String> keyValueMap)
    {
        getBlockNode(vector, true).putBlockProperties(keyValueMap);
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
        BlockPluginPropertiesNode blockNode = getBlockNode(vector, false);
        if (blockNode != null)
            return blockNode.removeBlockProperty(key);
        else
            return null;
    }

    public void removeBlockProperties(Vector vector, String... keys)
    {
        BlockPluginPropertiesNode blockNode = getBlockNode(vector, false);
        if (blockNode != null)
            blockNode.removeBlockProperties(keys);
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
        BlockPluginPropertiesNode blockNode = getBlockNode(vector, false);
        if (blockNode != null)
            return blockNode.getBlockProperty(key);
        else
            return null;
    }

    public Map<String, String> getBlockProperties(Vector vector, String... keys)
    {
        BlockPluginPropertiesNode blockNode = getBlockNode(vector, false);
        if (blockNode != null)
            return blockNode.getBlockProperties(keys);
        else
            return new HashMap<>();
    }

    @Override
    public BlockPluginPropertiesNode putBlockNode(Location location, BlockPluginPropertiesNode blockNode)
    {
        if (location == null)
            throw new NullArgumentException("location");

        return putBlockNode(location.toVector(), blockNode);
    }

    public BlockPluginPropertiesNode putBlockNode(Vector vector, BlockPluginPropertiesNode blockNode)
    {
        if (vector == null)
            throw new NullArgumentException("vector");
        if (blockNode == null)
            throw new NullArgumentException("blockNode");

        Vector key = getBlockKey(vector);
        return children.put(key, blockNode);
    }

    @Override
    public BlockPluginPropertiesNode removeBlockNode(Location location)
    {
        return removeBlockNode(location.toVector());
    }

    public BlockPluginPropertiesNode removeBlockNode(Vector vector)
    {
        Vector key = getBlockKey(vector);
        return children.remove(key);
    }

    @Override
    public BlockPluginPropertiesNode getBlockNode(Location location)
    {
        return getBlockNode(location.toVector());
    }
    public BlockPluginPropertiesNode getBlockNode(Vector vector)
    {
        return getBlockNode(vector, false);
    }

    public boolean isBlockInChildren(Location location)
    {
        return isBlockInChildren(location.toVector());
    }
    public boolean isBlockInChildren(Vector vector)
    {
        return getBlockNode(vector, false) != null;
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

    private BlockPluginPropertiesNode getBlockNode(Vector vector, boolean createIfNotExists)
    {
        Vector key = getBlockKey(vector);
        if (!children.containsKey(key) && createIfNotExists)
            children.put(key, new BlockPluginPropertiesNode(this));

        return children.get(key);
    }

    private Vector getBlockKey(Vector vector)
    {
        return vector.clone();
    }
}

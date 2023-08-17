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

public class ChunkNode extends Node implements IMutableNode, IMutableBlockNodeParent
{
    protected static final String SERIALIZED_NAME_KEY = "blocks";

    @Expose
    @SerializedName(SERIALIZED_NAME_KEY)
    private HashMap<Vector, BlockNode> children;

    public ChunkNode()
    {
        super();
        children = new HashMap<>();
    }

    public ChunkNode(Node parent)
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
        return getBlockNode(vector, true).putBlockProperty(key, value);
    }

    @Override
    public String removeBlockProperty(Location location, String key)
    {
        return removeBlockProperty(location.toVector(), key);
    }

    public String removeBlockProperty(Vector vector, String key)
    {
        BlockNode blockNode = getBlockNode(vector, false);
        if (blockNode != null)
            return blockNode.removeBlockProperty(key);
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
        BlockNode blockNode = getBlockNode(vector, false);
        if (blockNode != null)
            return blockNode.getBlockProperty(key);
        else
            return null;
    }

    @Override
    public BlockNode putBlockNode(Location location, BlockNode blockNode)
    {
        if (location == null)
            throw new NullArgumentException("location");

        return putBlockNode(location.toVector(), blockNode);
    }

    public BlockNode putBlockNode(Vector vector, BlockNode blockNode)
    {
        if (vector == null)
            throw new NullArgumentException("vector");
        if (blockNode == null)
            throw new NullArgumentException("blockNode");

        Vector key = getBlockKey(vector);
        return children.put(key, blockNode);
    }

    @Override
    public BlockNode removeBlockNode(Location location)
    {
        return removeBlockNode(location.toVector());
    }

    public BlockNode removeBlockNode(Vector vector)
    {
        Vector key = getBlockKey(vector);
        return children.remove(key);
    }

    @Override
    public BlockNode getBlockNode(Location location)
    {
        return getBlockNode(location.toVector());
    }
    public BlockNode getBlockNode(Vector vector)
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
     * Returns a new list of contained {@link BlockNode} values
     * @return a list of contained {@link BlockNode} values
     */
    public List<BlockNode> getBlockValues()
    {
        return new ArrayList<>(children.values());
    }

    /**
     * Returns a new list of {@link BlockNode} key:value pairs
     * @return a list of property key:value pairs, with the key a representation of block location,
     * and the value a {@link BlockNode}
     */
    public List<Map.Entry<Vector, BlockNode>> getBlockEntries()
    {
        return new ArrayList<>(children.entrySet());
    }

    private BlockNode getBlockNode(Vector vector, boolean createIfNotExists)
    {
        Vector key = getBlockKey(vector);
        if (!children.containsKey(key) && createIfNotExists)
            children.put(key, new BlockNode(this));

        return children.get(key);
    }

    private Vector getBlockKey(Vector vector)
    {
        return vector.clone();
    }
}

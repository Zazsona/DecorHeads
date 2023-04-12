package com.zazsona.decorheads.crafting;

import org.bukkit.NamespacedKey;

import java.util.HashSet;

public class MetaRecipeCraftTree
{
    private MetaRecipeCraftTreeNode root;

    public MetaRecipeCraftTree(MetaRecipeCraftTreeNode root)
    {
        this.root = root;
    }

    public MetaRecipeCraftTree()
    {
        this.root = new MetaRecipeCraftTreeNode(null, null);
    }

    /**
     * Gets root
     *
     * @return root
     */
    public MetaRecipeCraftTreeNode getRoot()
    {
        return root;
    }
}

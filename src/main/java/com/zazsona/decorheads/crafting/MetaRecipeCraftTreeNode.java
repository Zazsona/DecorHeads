package com.zazsona.decorheads.crafting;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.*;

public class MetaRecipeCraftTreeNode
{
    private Comparator<MetaRecipeCraftTreeNode> childComparator = Comparator.comparing(o -> o.getIngredient().getMaterial());

    private MetaIngredient ingredient;
    private NamespacedKey associatedRecipeKey;
    private MetaRecipeCraftTreeNode parent;
    private List<MetaRecipeCraftTreeNode> children;

    public MetaRecipeCraftTreeNode(MetaIngredient ingredient, NamespacedKey associatedRecipeKey)
    {
        this.ingredient = ingredient;
        this.associatedRecipeKey = associatedRecipeKey;
        this.parent = null;
        this.children = new ArrayList<>();
    }

    public MetaRecipeCraftTreeNode(MetaIngredient ingredient, NamespacedKey associatedRecipeKey, List<MetaRecipeCraftTreeNode> children)
    {
        this.ingredient = ingredient;
        this.associatedRecipeKey = associatedRecipeKey;
        this.parent = null;
        this.children = new ArrayList<>(children);
        for (MetaRecipeCraftTreeNode child : this.children)
            child.setParent(this);
        this.children.sort(childComparator);
    }

    /**
     * Gets ingredient
     * @return ingredient
     */
    public MetaIngredient getIngredient()
    {
        return ingredient;
    }

    public void setIngredient(MetaIngredient ingredient)
    {
        this.ingredient = ingredient;
    }

    /**
     * Gets parent
     * @return parent
     */
    public MetaRecipeCraftTreeNode getParent()
    {
        return parent;
    }

    private void setParent(MetaRecipeCraftTreeNode parent)
    {
        this.parent = parent;
    }

    /**
     * Gets recipeKey
     *
     * @return recipeKey
     */
    public NamespacedKey getAssociatedRecipe()
    {
        return associatedRecipeKey;
    }

    public void setAssociatedRecipe(NamespacedKey key)
    {
        this.associatedRecipeKey = key;
    }

    public boolean isAssociatedRecipe(NamespacedKey recipeKey)
    {
        return associatedRecipeKey.equals(recipeKey);
    }

    /**
     * Gets children
     * @return children
     */
    public List<MetaRecipeCraftTreeNode> getChildren()
    {
        return Collections.unmodifiableList(children);
    }

    public boolean addChild(MetaRecipeCraftTreeNode node)
    {
        boolean success = children.add(node);
        if (success)
            node.setParent(this);
        children.sort(childComparator);
        return success;
    }

    public boolean removeChild(MetaRecipeCraftTreeNode node)
    {
        boolean success = children.remove(node);
        if (success)
            node.setParent(null);
        return success;
    }

    public boolean isChild(MetaRecipeCraftTreeNode node)
    {
        return children.contains(node);
    }

    public List<MetaRecipeCraftTreeNode> getChildrenOfType(Material searchMaterial)
    {
        int lowerIndex = 0;
        int upperIndex = children.size();

        while (lowerIndex < upperIndex)
        {
            int currentIndex = (lowerIndex + upperIndex) / 2;
            MetaRecipeCraftTreeNode currentNode = children.get(currentIndex);
            Material currentMaterial = currentNode.getIngredient().getMaterial();
            int currentCompare = currentMaterial.compareTo(searchMaterial);
            if (currentCompare < 0)
                lowerIndex = currentIndex + 1;
            else if (currentCompare > 0)
                upperIndex = currentIndex - 1;
            else
            {
                ArrayList<MetaRecipeCraftTreeNode> nodes = new ArrayList<>();
                int materialMatchStartIndex = currentIndex;
                int materialMatchEndIndex = currentIndex;
                for (int i = (currentCompare - 1); i >= 0; i--)
                {
                    if (children.get(i).getIngredient().getMaterial().equals(searchMaterial))
                        materialMatchStartIndex = i;
                    else
                        break;
                }
                for (int i = (currentCompare + 1); i < children.size(); i++)
                {
                    if (children.get(i).getIngredient().getMaterial().equals(searchMaterial))
                        materialMatchEndIndex = i;
                    else
                        break;
                }
                for (int i = materialMatchStartIndex; i <= materialMatchEndIndex; i++)
                    nodes.add(children.get(i));
                return nodes;
            }
        }

        return new ArrayList<>();
    }
}

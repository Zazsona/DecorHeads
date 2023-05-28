package com.zazsona.decorheads.crafting;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class MetaRecipeCraftTreeNode
{
    private Comparator<MetaRecipeCraftTreeNode> childComparator = (o1, o2) ->
    {
        MetaIngredient currentIngredient = o1.getIngredient();
        MetaIngredient incomingIngredient = o2.getIngredient();
        if (currentIngredient == null)
            return (incomingIngredient == null) ? 0 : -1;
        else
            return (incomingIngredient == null) ? 1 : currentIngredient.getMaterial().getKey().toString().compareTo(incomingIngredient.getMaterial().getKey().toString());
    };

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

    public MetaRecipeCraftTreeNode getChildByAssociatedRecipe(NamespacedKey recipeKey)
    {
        for (MetaRecipeCraftTreeNode child : children)
        {
            if (child.isAssociatedRecipe(recipeKey))
                return child;
        }
        return null;
    }

    public List<MetaRecipeCraftTreeNode> getChildrenOfType(ItemStack searchStack)
    {
        Material material = (searchStack == null) ? null : searchStack.getType();
        return getChildrenOfType(material);
    }

    public List<MetaRecipeCraftTreeNode> getChildrenOfType(Material searchMaterial)
    {
        //Bukkit.getLogger().info("Searching For: " + ((searchMaterial == null) ? "null" : searchMaterial.getKey()));
        int lowerIndex = 0;
        int upperIndex = children.size() - 1;

        while (lowerIndex <= upperIndex)
        {
            int currentIndex = (int) Math.floor((lowerIndex + upperIndex) / 2);
            MetaRecipeCraftTreeNode currentNode = children.get(currentIndex);
            MetaIngredient currentIngredient = currentNode.getIngredient();
            //Bukkit.getLogger().info("Current Index: " + currentIndex + " (Low: " + lowerIndex+"; High: "+upperIndex+") - "+((currentIngredient == null) ? "null" : currentIngredient.getMaterial().toString()));
            int currentCompare;
            if (currentIngredient == null)
                currentCompare = (searchMaterial == null) ? 0 : -1;
            else
                currentCompare = (searchMaterial == null) ? 1 : currentIngredient.getMaterial().getKey().toString().compareTo(searchMaterial.getKey().toString());

            if (currentCompare < 0)
                lowerIndex = currentIndex + 1;
            else if (currentCompare > 0)
                upperIndex = currentIndex - 1;
            else
            {
                ArrayList<MetaRecipeCraftTreeNode> nodes = new ArrayList<>();
                int materialMatchStartIndex = currentIndex;
                int materialMatchEndIndex = currentIndex;
                for (int i = (currentIndex - 1); i >= 0; i--)
                {
                    MetaIngredient childIngredient = children.get(i).getIngredient();
                    if ((childIngredient != null && childIngredient.getMaterial().equals(searchMaterial)) || (childIngredient == null && searchMaterial == null))
                        materialMatchStartIndex = i;
                    else
                        break;
                }
                for (int i = (currentIndex + 1); i < children.size(); i++)
                {
                    MetaIngredient childIngredient = children.get(i).getIngredient();
                    if ((childIngredient != null && childIngredient.getMaterial().equals(searchMaterial)) || (childIngredient == null && searchMaterial == null))
                        materialMatchEndIndex = i;
                    else
                        break;
                }
                for (int i = materialMatchStartIndex; i <= materialMatchEndIndex; i++)
                    nodes.add(children.get(i));

                //for (MetaRecipeCraftTreeNode treeNode : nodes)
                //    Bukkit.getLogger().info("Found: " + treeNode.getAssociatedRecipe().getKey());
                return nodes;
            }
        }

        return new ArrayList<>();
    }
}

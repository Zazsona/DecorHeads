package com.zazsona.decorheads.crafting;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class MetaIngredient
{
    private ItemStack itemStack;
    private IngredientValidityComparator comparator;

    public MetaIngredient(ItemStack itemStack, IngredientValidityComparator comparator)
    {
        this.itemStack = itemStack;
        this.comparator = comparator;
    }

    /**
     * Gets itemStack
     *
     * @return itemStack
     */
    public ItemStack getItemStack()
    {
        return itemStack;
    }

    /**
     * Gets material
     *
     * @return material
     */
    public Material getMaterial()
    {
        return itemStack.getType();
    }

    /**
     * Gets itemMeta
     *
     * @return itemMeta
     */
    public ItemMeta getItemMeta()
    {
        return itemStack.getItemMeta();
    }

    /**
     * Checks if the provided item stack passes the requirements for this ingredient
     * @param itemStack the item stack to check
     * @return true if passing
     */
    public boolean isItemStackIngredientMatch(ItemStack itemStack)
    {
        return comparator.compare(itemStack, this.itemStack);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetaIngredient that = (MetaIngredient) o;
        return itemStack.equals(that.itemStack) && comparator.equals(that.comparator);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(itemStack, comparator);
    }
}

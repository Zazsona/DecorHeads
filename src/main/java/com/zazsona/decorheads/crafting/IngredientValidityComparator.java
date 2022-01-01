package com.zazsona.decorheads.crafting;

import org.bukkit.inventory.ItemStack;

public interface IngredientValidityComparator
{
    /**
     * Check if a provided ingredient is valid against the implemented criteria.
     * @param ingredient the ingredient offered to check validity for
     * @param refIngredient the reference ingredient
     * @return true is the offered ingredient is valid.
     */
    boolean compare(ItemStack ingredient, ItemStack refIngredient);
}

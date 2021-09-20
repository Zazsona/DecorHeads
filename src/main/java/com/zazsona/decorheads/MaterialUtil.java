package com.zazsona.decorheads;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.CampfireRecipe;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.Recipe;

import java.util.*;

public class MaterialUtil
{
    private static final String UNINDEXED_ERROR_MESSAGE = "Materials have not been indexed. You must call indexMaterials().";
    private static boolean materialsIndexed = false;
    private static Set<Material> cookableItems = new HashSet<>();

    public static void indexMaterials()
    {
        Iterator<Recipe> iterator = Bukkit.getServer().recipeIterator();
        while (iterator.hasNext())
        {
            Recipe recipe = iterator.next();
            if (recipe instanceof CookingRecipe)
            {
                CookingRecipe cookingRecipe = (CookingRecipe) recipe;
                cookableItems.add(cookingRecipe.getInput().getType());
            }
        }
        materialsIndexed = true;
    }

    public static boolean isCookable(Material item)
    {
        if (!materialsIndexed)
            throw new IllegalStateException(UNINDEXED_ERROR_MESSAGE);
        return cookableItems.contains(item);
    }

    public static boolean isCookableFood(Material item)
    {
        if (!materialsIndexed)
            throw new IllegalStateException(UNINDEXED_ERROR_MESSAGE);
        if (item.isEdible())
        {
            return isCookable(item);
        }
        return false;
    }
}

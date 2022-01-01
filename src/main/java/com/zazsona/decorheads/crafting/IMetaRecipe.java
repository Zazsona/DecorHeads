package com.zazsona.decorheads.crafting;

import org.bukkit.Keyed;
import org.bukkit.inventory.Recipe;

public interface IMetaRecipe extends Keyed, Recipe
{
    Recipe getRawRecipe();
}

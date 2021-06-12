package com.zazsona.decorheads.headdata;

import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;

public class ShapelessCraftHead extends CraftHead
{
    private ShapelessRecipe shapelessRecipe;

    public ShapelessCraftHead(IHead head, ShapelessRecipe recipe)
    {
        super(head);
        this.shapelessRecipe = recipe;
    }

    @Override
    public Recipe getRecipe()
    {
        return shapelessRecipe;
    }
}

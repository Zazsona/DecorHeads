package com.zazsona.decorheads.headdata;

import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

public class ShapedCraftHead extends CraftHead
{
    private ShapedRecipe shapedRecipe;

    public ShapedCraftHead(IHead head, ShapedRecipe recipe)
    {
        super(head);
        this.shapedRecipe = recipe;
    }

    @Override
    public Recipe getRecipe()
    {
        return shapedRecipe;
    }
}

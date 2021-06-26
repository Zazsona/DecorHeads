package com.zazsona.decorheads.headsources;

import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.inventory.Recipe;

import java.util.Set;

public abstract class CraftHeadSource extends HeadSource
{
    private Recipe recipe;

    public CraftHeadSource(IHead head, HeadSourceType headSourceType, Recipe recipe)
    {
        super(head, headSourceType);
        this.recipe = recipe;
    }

    public Recipe getRecipe()
    {
        return recipe;
    }
}

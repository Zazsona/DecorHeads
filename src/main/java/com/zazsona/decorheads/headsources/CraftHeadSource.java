package com.zazsona.decorheads.headsources;

import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.inventory.Recipe;

import java.util.Set;

public abstract class CraftHeadSource extends HeadSource implements ICraftHeadSource
{
    private Recipe recipe;

    public CraftHeadSource(IHead head, Recipe recipe)
    {
        super(head);
        this.recipe = recipe;
    }

    @Override
    public Recipe getRecipe()
    {
        return recipe;
    }
}

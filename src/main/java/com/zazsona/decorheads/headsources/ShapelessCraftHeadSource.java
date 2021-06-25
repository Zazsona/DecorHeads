package com.zazsona.decorheads.headsources;

import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.inventory.Recipe;

import java.util.Set;

public class ShapelessCraftHeadSource extends CraftHeadSource
{
    public ShapelessCraftHeadSource(IHead head, Recipe recipe)
    {
        super(head, recipe);
    }

    @Override
    public HeadSourceType getSourceType()
    {
        return HeadSourceType.SHAPELESS_CRAFT;
    }
}

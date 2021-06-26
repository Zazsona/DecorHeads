package com.zazsona.decorheads.headsources;

import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.inventory.Recipe;

import java.util.Set;

public class ShapedCraftHeadSource extends CraftHeadSource
{
    public ShapedCraftHeadSource(IHead head, Recipe recipe)
    {
        super(head, HeadSourceType.SHAPED_CRAFT, recipe);
    }
}

package com.zazsona.decorheads.headsources;

import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.inventory.Recipe;

import java.util.Set;

public class ShapedCraftHeadSource extends CraftHeadSource
{
    public ShapedCraftHeadSource(IHead head, Recipe recipe)
    {
        super(head, recipe);
    }

    @Override
    public Set<HeadSourceType> getSourceTypes()
    {
        Set<HeadSourceType> sourceTypes = super.getSourceTypes();
        sourceTypes.add(HeadSourceType.SHAPED_CRAFT);
        return sourceTypes;
    }
}

package com.zazsona.decorheads.headsources.dropfilters;

import com.zazsona.decorheads.headsources.HeadSourceType;
import org.bukkit.Material;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class RecipeResultDropFilter extends DropSourceFilter
{
    private Set<Material> results = new HashSet<>();

    public RecipeResultDropFilter(Collection<Material> results)
    {
        if (results != null)
            this.results.addAll(results);
    }

    public Set<Material> getResults()
    {
        return results;
    }

    private boolean checkPass(Material result)
    {
        return (results.size() == 0 || results.contains(result));
    }

    @Override
    protected boolean passFilter(HeadSourceType sourceType, CraftItemEvent e)
    {
        Material result = e.getRecipe().getResult().getType();
        return checkPass(result);
    }

    @Override
    protected boolean passFilter(HeadSourceType sourceType, BrewEvent e)
    {
        for (ItemStack result : e.getContents())
        {
            Material resultType = result.getType();
            boolean passedFilter = checkPass(resultType);
            if (passedFilter)
                return true;
        }
        return false;
    }

    @Override
    protected boolean passFilter(HeadSourceType sourceType, BlockCookEvent e)
    {
        Material result = e.getResult().getType();
        return checkPass(result);
    }
}

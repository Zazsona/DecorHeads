package com.zazsona.decorheads.headsources.dropfilters;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
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
    protected boolean passFilter(CraftItemEvent e)
    {
        Material result = e.getRecipe().getResult().getType();
        return checkPass(result);
    }

    @Override
    protected boolean passFilter(BrewEvent e)
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
    protected boolean passFilter(FurnaceSmeltEvent e)
    {
        Material result = e.getResult().getType();
        return checkPass(result);
    }
}

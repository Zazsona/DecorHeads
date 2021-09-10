package com.zazsona.decorheads.headsources.dropfilters;

import com.zazsona.decorheads.headsources.HeadSourceType;
import org.bukkit.Material;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class RecipeIngredientsDropFilter extends DropSourceFilter
{
    private Set<Set<Material>> ingredientWhitelists = new HashSet<>();

    public RecipeIngredientsDropFilter(Collection<Collection<Material>> ingredientWhitelists)
    {
        if (ingredientWhitelists != null)
        {
            for (Collection<Material> ingredientsWhitelist : ingredientWhitelists)
            {
                Set<Material> ingredientsWhitelistSet = new HashSet<>();
                ingredientsWhitelistSet.addAll(ingredientsWhitelist);
                this.ingredientWhitelists.add(ingredientsWhitelistSet);
            }
        }
    }

    public Set<Set<Material>> getIngredientWhitelists()
    {
        return ingredientWhitelists;
    }

    private boolean checkPass(Collection<Material> ingredients)
    {
        if (ingredientWhitelists.size() == 0)
        {
            return true;
        }
        else if (ingredients.size() > 0)
        {
            for (Set<Material> ingredientWhitelist : ingredientWhitelists)
            {
                if (ingredientWhitelist.size() <= ingredients.size() && ingredients.containsAll(ingredientWhitelist))
                    return true;
            }
        }
        return false;
    }

    @Override
    protected boolean passFilter(HeadSourceType sourceType, CraftItemEvent e)
    {
        Set<Material> ingredients = new HashSet<>();
        for (ItemStack itemStack : e.getInventory().getMatrix())
        {
            if (itemStack != null)
            {
                Material material = itemStack.getType();
                if (!material.isAir())
                    ingredients.add(itemStack.getType());
            }
        }
        return checkPass(ingredients);
    }

    @Override
    protected boolean passFilter(HeadSourceType sourceType, BrewEvent e)
    {
        HashSet<Material> ingredients = new HashSet<>();
        ItemStack fuelStack = e.getContents().getFuel();
        ItemStack ingredientStack = e.getContents().getIngredient();
        if (fuelStack != null)
        {
            Material fuel = e.getContents().getFuel().getType();
            if (!fuel.isAir())
                ingredients.add(fuel);
        }
        if (ingredientStack != null)
        {
            Material ingredient = e.getContents().getIngredient().getType();
            if (!ingredient.isAir())
                ingredients.add(ingredient);
        }
        return checkPass(ingredients);
    }

    @Override
    protected boolean passFilter(HeadSourceType sourceType, FurnaceSmeltEvent e)
    {
        HashSet<Material> ingredients = new HashSet<>();
        if (e.getSource() != null && !e.getSource().getType().isAir())
            ingredients.add(e.getSource().getType());
        return checkPass(ingredients);
    }
}

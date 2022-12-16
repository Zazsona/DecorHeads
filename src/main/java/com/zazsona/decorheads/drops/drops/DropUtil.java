package com.zazsona.decorheads.drops.drops;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DropUtil
{
    private static Random r = new Random();

    /**
     * Performs a roll check against the chance to drop
     * @return true/false on head roll passed.
     */
    public static boolean rollDrop(double dropRate)
    {
        double roll = r.nextDouble() * 100.0f;
        return (roll < dropRate);
    }

    /**
     * Performs a roll check against the chance to drop multiple times
     * @param dropRate the chance for a drop
     * @param quantity the number of rolls to perform
     * @return the number of passed drop rolls
     */
    public static int rollDrops(double dropRate, int quantity)
    {
        int passed = 0;
        for (int i = 0; i < quantity; i++)
        {
            boolean pass = rollDrop(dropRate);
            if (pass)
                passed++;
        }
        return passed;
    }

    /**
     * Drops an item in the world at the location
     * @param location the location of the world to drop in
     * @param itemTemplate the item to drop
     * @return the dropped ItemStack
     */
    public static Item dropItem(@NotNull Location location, @NotNull ItemStack itemTemplate)
    {
        List<Item> stacks = dropItems(location, itemTemplate, 1);
        if (stacks.size() > 1)
            return stacks.get(0);
        else
            return null;
    }

    /**
     * Drops items in the world at the location based on how many of the specified rolls pass the provided drop rate.
     * @param location the location of the world to drop in
     * @param itemTemplate the item to drop
     * @param dropRate the chance for a head to drop
     * @param rolls the number of rolls to perform against the chance
     * @return the dropped ItemStack(s)
     */
    public static List<Item> dropItems(@NotNull Location location, @NotNull ItemStack itemTemplate, double dropRate, int rolls)
    {
        int totalToDrop = rollDrops(dropRate, rolls);
        return dropItems(location, itemTemplate, totalToDrop);
    }

    /**
     * Drops items in the world at the location
     * @param location the location of the world to drop in
     * @param itemTemplate the item to drop
     * @param quantity the number of items to drop
     * @return the dropped ItemStack(s)
     */
    public static List<Item> dropItems(@NotNull Location location, @NotNull ItemStack itemTemplate, int quantity)
    {
        if (quantity > 0)
        {
            List<Item> itemEntities = new ArrayList<>();
            int itemsToDrop = quantity;
            while (itemsToDrop > 0)
            {
                int stackSize = (itemsToDrop > itemTemplate.getMaxStackSize()) ? itemTemplate.getMaxStackSize() : itemsToDrop;
                ItemStack stack = itemTemplate.clone();
                stack.setAmount(stackSize);
                Item itemEntity = location.getWorld().dropItemNaturally(location, stack);
                itemsToDrop -= stackSize;
                itemEntities.add(itemEntity);
            }
            return itemEntities;
        }
        return new ArrayList<>();
    }
}

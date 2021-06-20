package com.zazsona.decorheads.headsources;

import com.zazsona.decorheads.Settings;
import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;
import java.util.Set;

public abstract class DropHeadSource extends HeadSource implements IDropHeadSource
{
    private static Random rand = new Random();

    public DropHeadSource(IHead head)
    {
        super(head);
    }

    /**
     * Performs a roll check against the chance to drop
     * @return true/false on head roll passed.
     */
    public boolean rollDrop()
    {
        double roll = ((rand.nextInt(9999)+1.0)/100.0);
        return (roll <= Settings.getDropChance(getHead().getKey()));
    }

    public ItemStack dropHead(World world, Location location)
    {
        if (rollDrop())
        {
            ItemStack headStack = getHead().createItem();
            world.dropItemNaturally(location, headStack);
            return headStack;
        }
        return null;
    }

    @Override
    public ItemStack onBlockBreak(BlockBreakEvent e)
    {
        return null;
    }

    @Override
    public ItemStack onEntityDeath(EntityDeathEvent e)
    {
        return null;
    }

    @Override
    public ItemStack onCraftItem(CraftItemEvent e)
    {
        return null;
    }

    @Override
    public ItemStack onBrew(BrewEvent e)
    {
        return null;
    }

    @Override
    public ItemStack onSmelt(FurnaceSmeltEvent e)
    {
        return null;
    }
}

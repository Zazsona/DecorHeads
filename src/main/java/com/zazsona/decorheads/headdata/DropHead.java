package com.zazsona.decorheads.headdata;

import com.zazsona.decorheads.Core;
import com.zazsona.decorheads.HeadManager;
import com.zazsona.decorheads.Permissions;
import com.zazsona.decorheads.Settings;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.Random;

abstract class DropHead extends HeadDecorator implements IDropHead
{
    private static Random rand = new Random();

    public DropHead(IHead head)
    {
        super(head);
    }

    @Override
    public boolean isObtainedThroughDrops()
    {
        return true;
    }

    /**
     * Performs a roll check against the chance to drop
     * @return true/false on head roll passed.
     */
    public boolean rollDrop()
    {
        double roll = ((rand.nextInt(9999)+1.0)/100.0);
        return (roll <= Settings.getDropChance(super.getKey()));
    }

    public ItemStack dropHead(World world, Location location)
    {
        if (rollDrop())
        {
            ItemStack headStack = head.createItem();
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

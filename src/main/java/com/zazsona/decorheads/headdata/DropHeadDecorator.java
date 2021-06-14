package com.zazsona.decorheads.headdata;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;

public abstract class DropHeadDecorator extends HeadDecorator implements IDropHead
{
    public DropHeadDecorator(IDropHead head)
    {
        super(head);
    }

    @Override
    public ItemStack onBlockBreak(BlockBreakEvent e)
    {
        return ((IDropHead) head).onBlockBreak(e);
    }

    @Override
    public ItemStack onEntityDeath(EntityDeathEvent e)
    {
        return ((IDropHead) head).onEntityDeath(e);
    }

    @Override
    public ItemStack onCraftItem(CraftItemEvent e)
    {
        return ((IDropHead) head).onCraftItem(e);
    }

    @Override
    public ItemStack onBrew(BrewEvent e)
    {
        return ((IDropHead) head).onBrew(e);
    }

    @Override
    public ItemStack onSmelt(FurnaceSmeltEvent e)
    {
        return ((IDropHead) head).onSmelt(e);
    }
}

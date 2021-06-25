package com.zazsona.decorheads.headsources;

import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class DropHeadSourceDecorator implements IDropHeadSource
{
    protected IDropHeadSource dropHeadSource;

    public DropHeadSourceDecorator(IDropHeadSource dropHeadSource)
    {
        this.dropHeadSource = dropHeadSource;
    }

    @Override
    public IHead getHead()
    {
        return dropHeadSource.getHead();
    }

    @Override
    public HeadSourceType getSourceType()
    {
        return dropHeadSource.getSourceType();
    }

    @Override
    public ItemStack onBlockBreak(BlockBreakEvent e)
    {
        return dropHeadSource.onBlockBreak(e);
    }

    @Override
    public ItemStack onEntityDeath(EntityDeathEvent e)
    {
        return dropHeadSource.onEntityDeath(e);
    }

    @Override
    public ItemStack onCraftItem(CraftItemEvent e)
    {
        return dropHeadSource.onCraftItem(e);
    }

    @Override
    public ItemStack onBrew(BrewEvent e)
    {
        return dropHeadSource.onBrew(e);
    }

    @Override
    public ItemStack onSmelt(FurnaceSmeltEvent e)
    {
        return dropHeadSource.onSmelt(e);
    }
}

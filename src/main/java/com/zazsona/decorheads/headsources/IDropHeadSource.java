package com.zazsona.decorheads.headsources;

import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;

public interface IDropHeadSource extends IHeadSource, Listener
{
    public ItemStack onBlockBreak(BlockBreakEvent e);

    public ItemStack onEntityDeath(EntityDeathEvent e);

    public ItemStack onCraftItem(CraftItemEvent e);

    public ItemStack onBrew(BrewEvent e);

    public ItemStack onSmelt(FurnaceSmeltEvent e);
}

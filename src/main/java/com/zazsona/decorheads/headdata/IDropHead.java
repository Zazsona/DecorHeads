package com.zazsona.decorheads.headdata;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;

public interface IDropHead extends IHead, Listener
{
    public ItemStack onBlockBreak(BlockBreakEvent e);

    public ItemStack onEntityDeath(EntityDeathEvent e);

    public ItemStack onCraftItem(CraftItemEvent e);

    public ItemStack onBrew(BrewEvent e);

    public ItemStack onSmelt(FurnaceSmeltEvent e);
}

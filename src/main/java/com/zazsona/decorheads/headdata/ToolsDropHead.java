package com.zazsona.decorheads.headdata;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ToolsDropHead extends DropHeadDecorator
{
    private Set<Material> tools = new HashSet<Material>();

    public ToolsDropHead(IDropHead head, Collection<Material> tools)
    {
        super(head);
        if (tools != null)
            this.tools.addAll(tools);
    }

    @Override
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public ItemStack onBlockBreak(BlockBreakEvent e)
    {
        ItemStack tool = e.getPlayer().getInventory().getItemInMainHand();
        if (tools.size() == 0 || tools.contains(tool.getType()))
            return ((IDropHead) head).onBlockBreak(e);
        else
            return null;
    }

    @Override
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public ItemStack onEntityDeath(EntityDeathEvent e)
    {
        Player killer = e.getEntity().getKiller();
        ItemStack tool = (killer != null) ? killer.getInventory().getItemInMainHand() : new ItemStack(Material.AIR);
        if (tools.size() == 0 || tools.contains(tool.getType()))
            return ((IDropHead) head).onEntityDeath(e);
        else
            return null;

    }
}

package com.zazsona.decorheads.headdata;

import com.zazsona.decorheads.Core;
import com.zazsona.decorheads.HeadDropListener;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class BlockDropHead extends DropHead implements Listener
{
    private Set<Material> blocks = new HashSet<Material>();

    public BlockDropHead(IHead head, Collection<Material> blocks)
    {
        super(head);
        if (blocks != null)
            this.blocks.addAll(blocks);
    }

    @Override
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public ItemStack onBlockBreak(BlockBreakEvent e)
    {
        if ((blocks.size() == 0 || blocks.contains(e.getBlock().getType())))
        {
            World world = e.getBlock().getWorld();
            Location location = e.getBlock().getLocation();
            return super.dropHead(world, location);
        }
        return null;
    }
}

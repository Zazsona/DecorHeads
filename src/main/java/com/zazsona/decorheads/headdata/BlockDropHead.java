package com.zazsona.decorheads.headdata;

import com.zazsona.decorheads.Core;
import com.zazsona.decorheads.HeadDropListener;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class BlockDropHead extends DropHead implements Listener
{
    private Set<Material> blocks = new HashSet<Material>();
    private Set<Material> tools = new HashSet<Material>();

    public BlockDropHead(IHead head, Collection<Material> blocks)
    {
        super(head);
        if (blocks != null)
            this.blocks.addAll(blocks);
    }

    public BlockDropHead(IHead head, Collection<Material> blocks, Collection<Material> requiredTools)
    {
        super(head);
        if (blocks != null)
            this.blocks.addAll(blocks);
        if (requiredTools != null)
            this.tools.addAll(requiredTools);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e)
    {
        if ((blocks.size() == 0 || blocks.contains(e.getBlock().getType())) && rollDrop())
        {
            ItemStack tool = e.getPlayer().getInventory().getItemInMainHand();
            if (tools.size() == 0 || tools.contains(tool.getType()))
            {
                World world = e.getBlock().getWorld();
                Location location = e.getBlock().getLocation();
                world.dropItemNaturally(location, head.createItem());
            }
        }
    }
}

package com.Zazsona;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class PlacedHeadRetriever implements Listener
{
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent e)
    {
        Block block = e.getBlock();
        if (block.getType() == Material.PLAYER_HEAD && block.hasMetadata("DecorHeadsID"))
        {
            e.setDropItems(false);
            String headName = block.getMetadata("DecorHeadsID").get(0).asString();
            ItemStack item = HeadManager.getSkull(HeadManager.getHeadByName(headName));
            block.getWorld().dropItemNaturally(block.getLocation(), item);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlaced(BlockPlaceEvent e)
    {
        Block block = e.getBlock();
        if (block.getType() == Material.PLAYER_HEAD)
        {
            block.setMetadata("DecorHeadsID", new FixedMetadataValue(Core.getPlugin(Core.class), e.getItemInHand().getItemMeta().getDisplayName()));
        }
    }
}

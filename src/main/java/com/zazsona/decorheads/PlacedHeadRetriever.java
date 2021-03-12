package com.zazsona.decorheads;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * Class to ensure head names are retained after placing.
 */
public class PlacedHeadRetriever implements Listener
{
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent e)
    {
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE)
        {
            Block block = e.getBlock();
            if (block.getType() == Material.PLAYER_HEAD && block.hasMetadata("DecorHeadsID"))
            {
                e.setDropItems(false);
                String headName = block.getMetadata("DecorHeadsID").get(0).asString();
                ItemStack item = HeadManager.getSkull(HeadManager.getHeadByName(headName));
                block.getWorld().dropItemNaturally(block.getLocation(), item);
                block.removeMetadata("DecorHeadsID", Core.getPlugin(Core.class));
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlaced(BlockPlaceEvent e)
    {
        Block block = e.getBlock();
        if (block.getType() == Material.PLAYER_HEAD && HeadManager.getHeadByName(e.getItemInHand().getItemMeta().getDisplayName()) != null)
        {
            block.setMetadata("DecorHeadsID", new FixedMetadataValue(Core.getPlugin(Core.class), e.getItemInHand().getItemMeta().getDisplayName()));
        }
    }
}

package com.zazsona.decorheads;

import org.bukkit.ChatColor;
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
    public static String ID_KEY = "DecorHeadsID";

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e)
    {
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE)
        {
            Block block = e.getBlock();
            if (block.hasMetadata(ID_KEY))
            {
                e.setDropItems(false);
                ItemStack headStack = null;
                if (block.getType() == Material.PLAYER_HEAD)
                {
                    String headName = block.getMetadata("DecorHeadsID").get(0).asString();
                    headStack = HeadManager.getSkull(HeadManager.getHeadByName(headName));
                }
                else if (block.getType() == Material.ZOMBIE_HEAD)
                    headStack = HeadManager.getSkull(HeadManager.HeadType.Zombie);
                else if (block.getType() == Material.SKELETON_SKULL)
                    headStack = HeadManager.getSkull(HeadManager.HeadType.Skeleton);
                else if (block.getType() == Material.CREEPER_HEAD)
                    headStack = HeadManager.getSkull(HeadManager.HeadType.Creeper);
                else if (block.getType() == Material.WITHER_SKELETON_SKULL)
                    headStack = HeadManager.getSkull(HeadManager.HeadType.WitherSkeleton);

                if (headStack != null)
                    block.getWorld().dropItemNaturally(block.getLocation(), headStack);
                block.removeMetadata("DecorHeadsID", Core.getPlugin(Core.class));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlaced(BlockPlaceEvent e)
    {
        Block block = e.getBlock();
        if (e.getItemInHand().getItemMeta().getLore().contains(Core.PLUGIN_LORE))
        {
            block.setMetadata(ID_KEY, new FixedMetadataValue(Core.getPlugin(Core.class), e.getItemInHand().getItemMeta().getDisplayName()));
        }
    }
}

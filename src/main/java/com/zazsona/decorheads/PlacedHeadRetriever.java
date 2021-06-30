package com.zazsona.decorheads;

import com.zazsona.decorheads.config.HeadLoader;
import com.zazsona.decorheads.headdata.Head;
import com.zazsona.decorheads.headdata.IHead;
import com.zazsona.decorheads.headdata.PlayerHead;
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
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

/**
 * Class to ensure head names are retained after placing.
 */
public class PlacedHeadRetriever implements Listener
{
    public static String ID_KEY = "DecorHeadsID";
    public static String PLAYER_ID_KEY = "DecorHeadsPlayerID";

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e)
    {
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE)
        {
            Block block = e.getBlock();
            if (block.hasMetadata(ID_KEY))
            {
                String headKey = block.getMetadata(ID_KEY).get(0).asString();
                IHead head = HeadLoader.getInstance().getLoadedHeads().get(headKey);
                if (head != null)
                {
                    e.setDropItems(false);
                    ItemStack item;
                    if (head instanceof PlayerHead)
                    {
                        PlayerHead playerHead = (PlayerHead) head;
                        String uuid = block.getMetadata(PLAYER_ID_KEY).get(0).asString();
                        item = playerHead.createItem(UUID.fromString(uuid));
                    }
                    else
                        item = head.createItem();

                    block.getWorld().dropItemNaturally(block.getLocation(), item);
                }
                block.removeMetadata(ID_KEY, Core.getPlugin(Core.class));
                block.removeMetadata(PLAYER_ID_KEY, Core.getPlugin(Core.class));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlaced(BlockPlaceEvent e)
    {
        ItemStack placedItem = e.getItemInHand();
        if (placedItem != null)
        {
            PersistentDataContainer dataContainer = placedItem.getItemMeta().getPersistentDataContainer();
            String headKey = dataContainer.get(Head.getSkullHeadKeyKey(), PersistentDataType.STRING);
            Block block = e.getBlock();
            block.setMetadata(ID_KEY, new FixedMetadataValue(Core.getPlugin(Core.class), headKey));
            if (dataContainer.has(PlayerHead.getSkullUUIDKey(), PersistentDataType.STRING))
            {
                String uuid = dataContainer.get(PlayerHead.getSkullUUIDKey(), PersistentDataType.STRING);
                block.setMetadata(PLAYER_ID_KEY, new FixedMetadataValue(Core.getPlugin(Core.class), uuid));
            }
        }
    }
}

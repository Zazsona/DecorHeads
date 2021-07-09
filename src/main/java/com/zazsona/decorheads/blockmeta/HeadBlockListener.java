package com.zazsona.decorheads.blockmeta;

import com.zazsona.decorheads.config.HeadLoader;
import com.zazsona.decorheads.headdata.Head;
import com.zazsona.decorheads.headdata.IHead;
import com.zazsona.decorheads.headdata.PlayerHead;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

/**
 * Class to ensure head names are retained after placing.
 */
public class HeadBlockListener implements Listener
{
    public static String ID_KEY = "HeadId";
    public static String PLAYER_ID_KEY = "HeadPlayerId";

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e)
    {
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE)
        {
            BlockMetaLogger metaLogger = BlockMetaLogger.getInstance();
            Block block = e.getBlock();
            Location location = block.getLocation();
            if (metaLogger.isMetadataSet(location, ID_KEY))
            {
                String headKey = metaLogger.getMetadata(location, ID_KEY);
                IHead head = HeadLoader.getInstance().getLoadedHeads().get(headKey);
                if (head != null)
                {
                    e.setDropItems(false);
                    ItemStack item;
                    if (head instanceof PlayerHead)
                    {
                        PlayerHead playerHead = (PlayerHead) head;
                        String uuid = metaLogger.getMetadata(location, PLAYER_ID_KEY);
                        item = playerHead.createItem(UUID.fromString(uuid));
                    }
                    else
                        item = head.createItem();
                    block.getWorld().dropItemNaturally(block.getLocation(), item);
                }
                metaLogger.removeMetadata(location, ID_KEY);
                metaLogger.removeMetadata(location, PLAYER_ID_KEY);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlaced(BlockPlaceEvent e)
    {
        BlockMetaLogger metaLogger = BlockMetaLogger.getInstance();
        ItemStack placedItem = e.getItemInHand();
        if (placedItem != null && placedItem.getType() == Material.PLAYER_HEAD)
        {
            PersistentDataContainer dataContainer = placedItem.getItemMeta().getPersistentDataContainer();
            if (dataContainer.has(Head.getSkullHeadKeyKey(), PersistentDataType.STRING))
            {
                String headKey = dataContainer.get(Head.getSkullHeadKeyKey(), PersistentDataType.STRING);
                Block block = e.getBlock();
                Location location = block.getLocation();
                metaLogger.setMetadata(location, ID_KEY, headKey);
                if (dataContainer.has(PlayerHead.getSkullUUIDKey(), PersistentDataType.STRING))
                {
                    String uuid = dataContainer.get(PlayerHead.getSkullUUIDKey(), PersistentDataType.STRING);
                    metaLogger.setMetadata(location, PLAYER_ID_KEY, uuid);
                }
            }
        }
    }
}

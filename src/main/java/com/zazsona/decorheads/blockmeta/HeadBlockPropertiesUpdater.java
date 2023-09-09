package com.zazsona.decorheads.blockmeta;

import com.zazsona.decorheads.blockmeta.library.node.IMutableBlockPluginPropertiesNode;
import com.zazsona.decorheads.event.head.*;
import com.zazsona.decorheads.headdata.IHead;
import com.zazsona.decorheads.headdata.PlayerHead;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class HeadBlockPropertiesUpdater implements Listener
{
    protected IMutableBlockPluginPropertiesNode blockProperties;

    public HeadBlockPropertiesUpdater(IMutableBlockPluginPropertiesNode blockProperties)
    {
        this.blockProperties = blockProperties;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onHeadPlaced(HeadPlaceEvent e)
    {
        IHead head = e.getHead();
        ItemStack headItem = e.getPlacedItem();
        PersistentDataContainer dataContainer = headItem.getItemMeta().getPersistentDataContainer();
        Block block = e.getBlock();
        Location blockLoc = block.getLocation();

        blockProperties.putBlockProperty(blockLoc, HeadBlockUtil.HEAD_ID_KEY, head.getKey());
        if (dataContainer.has(PlayerHead.getSkullTextureKey(), PersistentDataType.STRING))
        {
            String texture = dataContainer.get(PlayerHead.getSkullTextureKey(), PersistentDataType.STRING);
            blockProperties.putBlockProperty(blockLoc, HeadBlockUtil.HEAD_TEXTURE_KEY, texture);
        }
        if (dataContainer.has(PlayerHead.getSkullUUIDKey(), PersistentDataType.STRING))
        {
            String uuid = dataContainer.get(PlayerHead.getSkullUUIDKey(), PersistentDataType.STRING);
            blockProperties.putBlockProperty(blockLoc, HeadBlockUtil.PLAYER_ID_KEY, uuid);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onHeadDestroy(HeadDestroyEvent e)
    {
        Block block = e.getBlock();
        Location blockLoc = block.getLocation();

        blockProperties.removeBlockProperty(blockLoc, HeadBlockUtil.HEAD_ID_KEY);
        blockProperties.removeBlockProperty(blockLoc, HeadBlockUtil.HEAD_TEXTURE_KEY);
        blockProperties.removeBlockProperty(blockLoc, HeadBlockUtil.PLAYER_ID_KEY);
    }
}

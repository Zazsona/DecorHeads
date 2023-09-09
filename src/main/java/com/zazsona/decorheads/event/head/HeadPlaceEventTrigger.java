package com.zazsona.decorheads.event.head;

import com.zazsona.decorheads.blockmeta.library.node.IMutableBlockPluginPropertiesNode;
import com.zazsona.decorheads.config.HeadRepository;
import com.zazsona.decorheads.headdata.Head;
import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class HeadPlaceEventTrigger implements Listener
{
    protected IMutableBlockPluginPropertiesNode blockProperties;

    public HeadPlaceEventTrigger(IMutableBlockPluginPropertiesNode blockProperties)
    {
        this.blockProperties = blockProperties;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockPlaced(BlockPlaceEvent e)
    {
        // Dependency checks
        ItemStack placedItem = e.getItemInHand();
        if (placedItem.getType() != Material.PLAYER_HEAD && placedItem.getType() != Material.PLAYER_WALL_HEAD)
            return;

        PersistentDataContainer dataContainer = placedItem.getItemMeta().getPersistentDataContainer();
        if (!dataContainer.has(Head.getSkullHeadKeyKey(), PersistentDataType.STRING))
            return;

        String headKey = dataContainer.get(Head.getSkullHeadKeyKey(), PersistentDataType.STRING);
        IHead head = HeadRepository.getLoadedHeads().get(headKey);

        HeadPlaceEvent event = new HeadPlaceEvent(e.getBlockPlaced(), head, placedItem);
        Bukkit.getPluginManager().callEvent(event);
        e.setCancelled(event.isCancelled()); // This handler only fires if the event isn't cancelled already, so this can't "uncancel"
    }
}

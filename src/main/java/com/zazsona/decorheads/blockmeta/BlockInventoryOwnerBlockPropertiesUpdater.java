package com.zazsona.decorheads.blockmeta;

import com.zazsona.decorheads.blockmeta.library.node.IMutableBlockPluginPropertiesNode;
import com.zazsona.decorheads.event.BlockInventoryOwnerUpdateEvent;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import java.util.UUID;

public class BlockInventoryOwnerBlockPropertiesUpdater implements Listener
{
    protected IMutableBlockPluginPropertiesNode blockProperties;

    public BlockInventoryOwnerBlockPropertiesUpdater(IMutableBlockPluginPropertiesNode blockProperties)
    {
        this.blockProperties = blockProperties;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockInventoryOwnerUpdate(BlockInventoryOwnerUpdateEvent e)
    {
        UUID newOwnerId = e.getNewOwnerPlayerId();
        Block block = e.getBlock();
        Location blockLoc = block.getLocation();

        if (newOwnerId != null)
            blockProperties.putBlockProperty(blockLoc, DecorHeadsBlockPluginPropertyKey.INVENTORY_OWNER_PLAYER_ID_KEY, newOwnerId.toString());
        else
            blockProperties.removeBlockProperty(blockLoc, DecorHeadsBlockPluginPropertyKey.INVENTORY_OWNER_PLAYER_ID_KEY);
    }
}

package com.zazsona.decorheads.blockmeta;

import com.zazsona.decorheads.blockmeta.library.node.IBlockPluginPropertiesNode;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;

import java.util.UUID;

public class InventoryBlockUtil
{
    public static final String INVENTORY_OWNER_PLAYER_ID_KEY = "inventoryOwnerPlayerId";

    /**
     * Gets the player who owns this inventory
     * @param properties the container for block plugin properties
     * @param block the target block
     * @return the player, or null if there is no registered owner in the properties
     */
    public static OfflinePlayer getInventoryOwner(IBlockPluginPropertiesNode properties, Block block)
    {
        String ownerId = properties.getBlockProperty(block.getLocation(), INVENTORY_OWNER_PLAYER_ID_KEY);
        if (ownerId == null)
            return null;

        UUID ownerUuid = UUID.fromString(ownerId);
        return Bukkit.getOfflinePlayer(ownerUuid);
    }
}

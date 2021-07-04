package com.zazsona.decorheads.headsources;

import com.zazsona.decorheads.BlockInventoryOwnerListener;
import com.zazsona.decorheads.config.PluginConfig;
import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.inventory.ItemStack;

public class BrewDropHeadSource extends DropHeadSource
{
    public BrewDropHeadSource(IHead head, double dropRate)
    {
        super(head, HeadSourceType.BREW_DROP, dropRate);
    }

    protected BrewDropHeadSource(IHead head, HeadSourceType sourceType, double dropRate)
    {
        super(head, sourceType, dropRate);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public ItemStack onBrewComplete(BrewEvent e)
    {
        if (PluginConfig.isDropSourceEnabled(getSourceType()) && passFilters(e))
        {
            OfflinePlayer offlinePlayer = BlockInventoryOwnerListener.getInstance().getOwningPlayer(e.getBlock());
            Player player = (offlinePlayer != null) ? offlinePlayer.getPlayer() : null;
            World world = e.getBlock().getWorld();
            Location location = e.getBlock().getLocation();
            return super.dropHead(world, location, player, null);
        }
        return null;
    }
}

package com.zazsona.decorheads.headsources;

import com.zazsona.decorheads.blockmeta.BlockInventoryOwnerListener;
import com.zazsona.decorheads.config.PluginConfig;
import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockCookEvent;

public class SmeltDropHeadSource extends DropHeadSource
{
    public SmeltDropHeadSource(IHead head, double dropRate)
    {
        super(head, HeadSourceType.SMELT_DROP, dropRate);
    }

    protected SmeltDropHeadSource(IHead head, HeadSourceType sourceType, double dropRate)
    {
        super(head, sourceType, dropRate);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSmeltComplete(BlockCookEvent e)
    {
        if (PluginConfig.isDropSourceEnabled(getSourceType()) && passFilters(e))
        {
            OfflinePlayer offlinePlayer = BlockInventoryOwnerListener.getInstance().getOwningPlayer(e.getBlock());
            Player player = (offlinePlayer != null) ? offlinePlayer.getPlayer() : null;
            World world = e.getBlock().getWorld();
            Location location = e.getBlock().getLocation();
            super.dropHeads(world, location, player, null, getBaseDropRate(), 1);
        }
    }
}

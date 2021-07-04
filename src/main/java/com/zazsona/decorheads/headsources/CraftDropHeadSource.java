package com.zazsona.decorheads.headsources;

import com.zazsona.decorheads.config.PluginConfig;
import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class CraftDropHeadSource extends DropHeadSource
{
    public CraftDropHeadSource(IHead head, double dropRate)
    {
        super(head, HeadSourceType.CRAFT_DROP, dropRate);
    }

    protected CraftDropHeadSource(IHead head, HeadSourceType sourceType, double dropRate)
    {
        super(head, sourceType, dropRate);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public ItemStack onItemCrafted(CraftItemEvent e)    //TODO: Consider multiple rolls if multiple items are crafted
    {
        if (PluginConfig.isDropSourceEnabled(getSourceType()) && passFilters(e))
        {
            Player player = (Player) e.getWhoClicked();
            World world = e.getWhoClicked().getWorld();
            Location location = e.getInventory().getLocation();
            return super.dropHead(world, location, player, null);
        }
        return null;
    }
}

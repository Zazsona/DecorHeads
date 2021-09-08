package com.zazsona.decorheads.headsources;

import com.zazsona.decorheads.config.PluginConfig;
import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

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
    public ItemStack onItemCrafted(CraftItemEvent e)
    {
        if (PluginConfig.isDropSourceEnabled(getSourceType()) && passFilters(e))
        {
            Player player = (Player) e.getWhoClicked();
            World world = e.getWhoClicked().getWorld();
            Location location = e.getInventory().getLocation();

            int totalCrafted = 0;
            if (e.isShiftClick())
            {
                ItemStack[] matrixItems = e.getInventory().getMatrix();
                int lowestQuantity = Integer.MAX_VALUE;
                for (ItemStack itemStack : matrixItems)
                {
                    if (itemStack != null && itemStack.getType() == Material.AIR && itemStack.getAmount() < lowestQuantity)
                        lowestQuantity = itemStack.getAmount();
                }
                totalCrafted = lowestQuantity * e.getRecipe().getResult().getAmount();
            }
            else
                totalCrafted = e.getRecipe().getResult().getAmount();

            int totalHeadDrops = 0;
            for (int i = 0; i < totalCrafted; i++)
            {
                if (super.rollDrop(getBaseDropRate()))
                    totalHeadDrops++;
            }

            return super.dropHead(world, location, player, null, totalHeadDrops);
        }
        return null;
    }
}

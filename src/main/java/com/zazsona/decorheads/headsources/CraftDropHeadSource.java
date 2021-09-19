package com.zazsona.decorheads.headsources;

import com.zazsona.decorheads.config.PluginConfig;
import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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
    public void onItemCrafted(CraftItemEvent e)
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
                boolean quantitySet = false;
                for (ItemStack itemStack : matrixItems)
                {
                    if (itemStack != null && itemStack.getType() != Material.AIR && itemStack.getAmount() < lowestQuantity)
                    {
                        lowestQuantity = itemStack.getAmount();
                        quantitySet = true;
                    }
                }
                totalCrafted = (quantitySet) ? lowestQuantity * e.getRecipe().getResult().getAmount() : 0;
            }
            else
                totalCrafted = e.getRecipe().getResult().getAmount();

            int totalHeadDrops = 0;
            for (int i = 0; i < totalCrafted; i++)
            {
                if (super.rollDrop(getBaseDropRate()))
                    totalHeadDrops++;
            }
            super.dropHeads(world, location, player, null, totalHeadDrops);
        }
    }
}

package com.zazsona.decorheads.headsources;

import com.zazsona.decorheads.Core;
import com.zazsona.decorheads.config.PluginConfig;
import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
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
        if (PluginConfig.isDropSourceEnabled(getSourceType()) && e.getAction() != InventoryAction.NOTHING && passFilters(e))
        {
            Player player = (Player) e.getWhoClicked();
            World world = e.getWhoClicked().getWorld();
            Location location = e.getInventory().getLocation();

            if (!e.isShiftClick())
            {
                int totalCrafted = e.getRecipe().getResult().getAmount();
                super.dropHeads(world, location, player, null, getBaseDropRate(), totalCrafted);
            }
            else
            {
                Material resultMaterial = e.getRecipe().getResult().getType();
                int preCraftQuantity = 0;
                ItemStack[] inventoryContents = e.getWhoClicked().getInventory().getStorageContents();
                for (ItemStack stack : inventoryContents)
                {
                    if (stack != null && stack.getType() == resultMaterial)
                        preCraftQuantity += stack.getAmount();
                }

                final int quantityPreviouslyHeld = preCraftQuantity;
                Bukkit.getScheduler().runTaskLater(Core.getSelfPlugin(), () ->
                {
                    int quantityHeld = 0;
                    ItemStack[] updatedInventoryContents = e.getWhoClicked().getInventory().getStorageContents();
                    for (ItemStack stack : updatedInventoryContents)
                    {
                        if (stack != null && stack.getType() == resultMaterial)
                            quantityHeld += stack.getAmount();
                    }
                    int totalCrafted = quantityHeld - quantityPreviouslyHeld;
                    super.dropHeads(world, location, player, null, getBaseDropRate(), totalCrafted);
                }, 1);
            }
        }
    }
}

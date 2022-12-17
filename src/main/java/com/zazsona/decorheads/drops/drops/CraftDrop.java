package com.zazsona.decorheads.drops.drops;

import com.zazsona.decorheads.Core;
import com.zazsona.decorheads.Permissions;
import com.zazsona.decorheads.config.DropType;
import com.zazsona.decorheads.config.PluginConfig;
import com.zazsona.decorheads.headdata.IHead;
import com.zazsona.decorheads.drops.filters.IDropFilter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CraftDrop extends Drop
{
    public CraftDrop(@NotNull String key, double dropRate, @NotNull ItemStack result)
    {
        super(key, dropRate, result);
    }

    public CraftDrop(@NotNull String key, double dropRate, @NotNull IHead result)
    {
        super(key, dropRate, result);
    }

    public CraftDrop(@NotNull String key, double dropRate, @NotNull ItemStack result, @NotNull List<IDropFilter> filters)
    {
        super(key, dropRate, result, filters);
    }

    public CraftDrop(@NotNull String key, double dropRate, @NotNull IHead result, @NotNull List<IDropFilter> filters)
    {
        super(key, dropRate, result, filters);
    }

    @Override
    public DropType getDropType()
    {
        return DropType.CRAFT;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCraftComplete(CraftItemEvent e)
    {
        try
        {
            Player player = (Player) e.getWhoClicked();
            boolean enabledPass = (PluginConfig.isPluginEnabled() && PluginConfig.isDropsEnabled() && PluginConfig.isDropTypeEnabled(getDropType()));
            boolean rollPass = DropUtil.rollDrop(getDropRate());
            boolean permissionsPass = player.hasPermission(Permissions.DROP_HEADS);

            if (enabledPass && rollPass && permissionsPass && isFiltersPass(e))
            {
                Location location = e.getInventory().getLocation();
                ItemStack result = getResult(player.getUniqueId());

                if (!e.isShiftClick())
                {
                    int totalCrafted = e.getRecipe().getResult().getAmount();
                    DropUtil.dropItems(location, result, getDropRate(), totalCrafted);
                }
                else
                {
                    ItemStack craftResult = e.getRecipe().getResult();
                    int preCraftQuantity = 0;
                    ItemStack[] inventoryContents = e.getWhoClicked().getInventory().getStorageContents();
                    for (ItemStack stack : inventoryContents)
                    {
                        if (stack != null && stack.isSimilar(craftResult))
                            preCraftQuantity += stack.getAmount();
                    }

                    final int quantityPreviouslyHeld = preCraftQuantity;
                    Bukkit.getScheduler().runTaskLater(Core.getSelfPlugin(), () ->
                    {
                        int quantityHeld = 0;
                        ItemStack[] updatedInventoryContents = e.getWhoClicked().getInventory().getStorageContents();
                        for (ItemStack stack : updatedInventoryContents)
                        {
                            if (stack != null && stack.isSimilar(craftResult))
                                quantityHeld += stack.getAmount();
                        }
                        int totalCrafted = quantityHeld - quantityPreviouslyHeld;
                        DropUtil.dropItems(location, result, getDropRate(), totalCrafted);
                    }, 1);
                }
            }
        }
        catch (IllegalArgumentException ex)
        {
            Bukkit.getLogger().warning(String.format("[%s] %s drop event failed: %s", Core.PLUGIN_NAME, getDropType().toString(), ex.getMessage()));
        }
    }
}

package com.zazsona.decorheads;

import com.zazsona.decorheads.config.PluginConfig;
import com.zazsona.decorheads.headdata.Head;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class HeadCraftBlocker implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPrepareItemCraft(PrepareItemCraftEvent e)
    {
        if (e.getRecipe() != null && e.getRecipe().getResult() != null)
        {
            ItemStack resultingItem = e.getRecipe().getResult();
            if ((resultingItem.getType() == Material.PLAYER_HEAD || resultingItem.getType() == Material.PLAYER_WALL_HEAD) && resultingItem.getItemMeta().getPersistentDataContainer().has(Head.getSkullHeadKeyKey(), PersistentDataType.STRING))
            {
                HumanEntity craftingEntity = e.getView().getPlayer();
                boolean craftingEnabled = PluginConfig.isPluginEnabled() && PluginConfig.isCraftingEnabled();
                if (!craftingEntity.hasPermission(Permissions.CRAFT_HEADS) || (!craftingEnabled))
                    e.getInventory().setResult(null);
            }
        }
    }
}

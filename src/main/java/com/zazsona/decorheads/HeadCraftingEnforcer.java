package com.zazsona.decorheads;

import com.zazsona.decorheads.config.PluginConfig;
import com.zazsona.decorheads.headdata.Head;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class HeadCraftingEnforcer implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemCrafted(CraftItemEvent e)
    {
        ItemStack resultingItem = e.getRecipe().getResult();
        if ((resultingItem.getType() == Material.PLAYER_HEAD || resultingItem.getType() == Material.PLAYER_WALL_HEAD) && resultingItem.getItemMeta().getPersistentDataContainer().has(Head.getSkullHeadKeyKey(), PersistentDataType.STRING))
        {
            HumanEntity craftingEntity = e.getWhoClicked();
            boolean craftingEnabled = PluginConfig.isPluginEnabled() && PluginConfig.isCraftingEnabled();
            if (!craftingEntity.hasPermission(Permissions.CRAFT_HEADS) || (!craftingEnabled))
            {
                String message = (craftingEnabled) ? String.format("You do not have permission to craft heads from %s.", Core.PLUGIN_NAME) : String.format("%s head crafting is disabled.", Core.PLUGIN_NAME);
                craftingEntity.sendMessage(ChatColor.RED+message);
                e.setCancelled(true);
            }
        }
    }
}

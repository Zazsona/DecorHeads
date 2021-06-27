package com.zazsona.decorheads.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PreviewInventory implements Listener
{
    private Inventory inventory;

    public PreviewInventory(ItemStack previewItem)
    {
        inventory = Bukkit.createInventory(null, 9, "Preview");
        inventory.setItem(4, previewItem);
    }

    public void showInventory(HumanEntity entity)
    {
        entity.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryDragEvent e) //Stop players nicking the item
    {
        if (e.getInventory().equals(inventory))
            e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e)
    {
        if (e.getInventory().equals(inventory))
            e.setCancelled(true);
    }
}

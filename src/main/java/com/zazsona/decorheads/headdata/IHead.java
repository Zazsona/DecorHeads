package com.zazsona.decorheads.headdata;

import org.bukkit.inventory.ItemStack;

public interface IHead
{
    public String getKey();
    public String getName();
    public String getTextureEncoding();
    public ItemStack createItem();
}

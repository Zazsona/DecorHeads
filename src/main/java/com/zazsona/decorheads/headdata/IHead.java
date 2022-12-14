package com.zazsona.decorheads.headdata;

import org.bukkit.inventory.ItemStack;

public interface IHead
{
    String getKey();
    String getName();
    String getTexture();
    ItemStack createItem();

}

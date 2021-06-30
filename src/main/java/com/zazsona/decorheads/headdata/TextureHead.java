package com.zazsona.decorheads.headdata;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.zazsona.decorheads.Core;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.UUID;

public class TextureHead extends Head
{
    private String name;
    private String texture;

    public TextureHead(String key, String name, String texture)
    {
        super(key);
        this.name = name;
        this.texture = texture;
    }

    public String getName()
    {
        return name;
    }

    public String getTextureEncoding()
    {
        return texture;
    }

    @Override
    public ItemStack createItem()
    {
        return createSkull(name, texture);
    }
}

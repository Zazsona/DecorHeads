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
import java.util.logging.Level;

public class Head implements IHead
{
    private static final String PLUGIN_LORE = ChatColor.BLUE+"DecorHeads";
    private String key;
    private String name;
    private String texture;

    public Head(String key, String name, String texture)
    {
        this.key = key;
        this.name = name;
        this.texture = texture;
    }

    @Override
    public String getKey()
    {
        return key;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getTextureEncoding()
    {
        return texture;
    }

    @Override
    public ItemStack createItem()
    {
        try
        {
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
            UUID uuid = UUID.fromString("8a2de1bb-a9b2-4f00-b0e3-d360ad407928"); //Random UUID unassigned to any Minecraft account.
            GameProfile gameProfile = new GameProfile(uuid, getName());
            gameProfile.getProperties().put("textures", new Property("textures", texture));
            Field skullProfile = skullMeta.getClass().getDeclaredField("profile");
            skullProfile.setAccessible(true);
            skullProfile.set(skullMeta, gameProfile);
            skullMeta.setDisplayName(getName());
            addLore(skullMeta);
            skull.setItemMeta(skullMeta);
            return skull;
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            Bukkit.getLogger().log(Level.SEVERE, "INVALID HEAD ITEM \""+getName()+"\" - NO PROFILE FIELD!");
            return new ItemStack(Material.PLAYER_HEAD);
        }
    }

    private static ItemMeta addLore(ItemMeta meta)
    {
        ArrayList<String> loreList = new ArrayList<>();
        loreList.add(PLUGIN_LORE);
        meta.setLore(loreList);
        return meta;
    }

    @Override
    public boolean isObtainedThroughDrops()
    {
        return false;
    }

    @Override
    public boolean isObtainedThroughCreation()
    {
        return false;
    }
}

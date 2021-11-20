package com.zazsona.decorheads.headdata;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.zazsona.decorheads.Core;
import org.bukkit.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.UUID;

public abstract class Head implements IHead
{
    protected static final String PLUGIN_LORE = ChatColor.BLUE+"DecorHeads";
    protected static final String DEFAULT_UUID = "8a2de1bb-a9b2-4f00-b0e3-d360ad407928"; //Random UUID unassigned to any Minecraft account.
    protected static final String TEXTURES_KEY = "textures";
    protected static final String PROFILE_KEY = "profile";

    private String key;

    public Head(String key)
    {
        this.key = key;
    }

    @Override
    public String getKey()
    {
        return key;
    }

    public static NamespacedKey getSkullHeadKeyKey()
    {
        return new NamespacedKey(Core.getSelfPlugin(), "HeadKey");
    }

    protected ItemStack createSkull(String name, String texture)
    {
        try
        {
            UUID uuid = UUID.fromString(DEFAULT_UUID);
            GameProfile gameProfile = new GameProfile(uuid, name);
            gameProfile.getProperties().put(TEXTURES_KEY, new Property(TEXTURES_KEY, texture));
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
            Field skullProfile = skullMeta.getClass().getDeclaredField(PROFILE_KEY);
            skullProfile.setAccessible(true);
            skullProfile.set(skullMeta, gameProfile);
            skullMeta.setDisplayName(name);
            applyDecorHeadsSkullMeta(skullMeta);
            addDecorHeadsLore(skullMeta);
            skull.setItemMeta(skullMeta);
            return skull;
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            Bukkit.getLogger().severe(String.format("[%s] Could not create head %s", Core.PLUGIN_NAME, getKey()));
            return new ItemStack(Material.PLAYER_HEAD);
        }
    }

    protected ItemMeta applyDecorHeadsSkullMeta(ItemMeta meta)
    {
        PersistentDataContainer dataHolder = meta.getPersistentDataContainer();
        NamespacedKey headKeyKey = getSkullHeadKeyKey();
        dataHolder.set(headKeyKey, PersistentDataType.STRING, key);
        return meta;
    }

    protected static ItemMeta addDecorHeadsLore(ItemMeta meta)
    {
        ArrayList<String> loreList = new ArrayList<>();
        loreList.add(PLUGIN_LORE);
        meta.setLore(loreList);
        return meta;
    }
}

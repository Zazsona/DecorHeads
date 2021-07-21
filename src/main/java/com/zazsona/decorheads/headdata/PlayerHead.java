package com.zazsona.decorheads.headdata;

import com.google.gson.Gson;
import com.zazsona.decorheads.Core;
import com.zazsona.decorheads.DecorHeadsUtil;
import com.zazsona.decorheads.apiresponse.ProfileResponse;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

public class PlayerHead extends Head
{
    private static final String HEAD_NAME_FORMAT = "%s's Head";

    public PlayerHead(String key)
    {
        super(key);
    }

    public static NamespacedKey getSkullUUIDKey()
    {
        return new NamespacedKey(Core.getSelfPlugin(), "OwnerUUID");
    }

    public static NamespacedKey getSkullTextureKey()
    {
        return new NamespacedKey(Core.getSelfPlugin(), "Texture");
    }

    @Override
    public ItemStack createItem()
    {
        return new ItemStack(Material.PLAYER_HEAD);
    }

    public ItemStack createItem(String playerName)
    {
        try
        {
            String uuid = DecorHeadsUtil.fetchPlayerUUID(playerName);
            return createItem(UUID.fromString(uuid));
        }
        catch (IOException | NullPointerException e)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to get UUID for player: %s", Core.PLUGIN_NAME, playerName));
        }
        return new ItemStack(Material.PLAYER_HEAD);
    }

    public ItemStack createItem(OfflinePlayer player)
    {
        return createItem(player.getUniqueId());
    }

    public ItemStack createItem(UUID uuid)
    {
        try
        {
            ProfileResponse pr = DecorHeadsUtil.fetchPlayerProfile(uuid);
            String encodedTextureData = pr.getPropertyByName(TEXTURES_KEY).getValue();
            String textureDataJson = new String(Base64.getDecoder().decode(encodedTextureData));
            Gson gson = new Gson();
            ProfileTextureData textureData = gson.fromJson(textureDataJson, ProfileTextureData.class);
            textureData.setTimestamp(null);
            textureData.setProfileId(null); // Nulling the data removes it from the JSON output
            textureData.setProfileName(null);
            String skullTextureJson = gson.toJson(textureData);
            String encodedSkullTexture = new String(Base64.getEncoder().encode(skullTextureJson.getBytes(StandardCharsets.UTF_8)));
            ItemStack skull = createSkull(String.format(HEAD_NAME_FORMAT, pr.getName()), encodedSkullTexture);
            ItemMeta meta = applyDecorHeadsPlayerSkullMeta(skull.getItemMeta(), uuid.toString(), encodedSkullTexture);
            skull.setItemMeta(meta);
            return skull;
        }
        catch (IOException e)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to resolve player head for UUID: %s", Core.PLUGIN_NAME, uuid));
            return new ItemStack(Material.PLAYER_HEAD);
        }
    }

    public ItemStack createItem(UUID uuid, String texture)
    {
        try
        {
            ProfileResponse pr = DecorHeadsUtil.fetchPlayerProfile(uuid);
            ItemStack skull = createSkull(String.format(HEAD_NAME_FORMAT, pr.getName()), texture);
            ItemMeta meta = applyDecorHeadsPlayerSkullMeta(skull.getItemMeta(), uuid.toString(), texture);
            skull.setItemMeta(meta);
            return skull;
        }
        catch (IOException e)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to resolve player head for UUID: %s", Core.PLUGIN_NAME, uuid));
            return new ItemStack(Material.PLAYER_HEAD);
        }
    }

    protected ItemMeta applyDecorHeadsPlayerSkullMeta(ItemMeta meta, String uuid, String texture)
    {
        PersistentDataContainer dataHolder = meta.getPersistentDataContainer();
        NamespacedKey uuidKey = getSkullUUIDKey();
        dataHolder.set(uuidKey, PersistentDataType.STRING, uuid);
        NamespacedKey textureKey = getSkullTextureKey();
        dataHolder.set(textureKey, PersistentDataType.STRING, texture);
        return meta;
    }
}

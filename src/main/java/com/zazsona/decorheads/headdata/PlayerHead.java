package com.zazsona.decorheads.headdata;

import com.google.gson.Gson;
import com.mojang.authlib.GameProfile;
import com.zazsona.decorheads.Core;
import com.zazsona.decorheads.DecorHeadsUtil;
import com.zazsona.decorheads.apiresponse.NameUUIDResponse;
import com.zazsona.decorheads.apiresponse.ProfileResponse;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
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
            ItemMeta meta = assignHeadUUIDToItem(skull.getItemMeta(), uuid.toString());
            skull.setItemMeta(meta);
            return skull;
        }
        catch (IOException e)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to resolve player head for UUID: %s", Core.PLUGIN_NAME, uuid));
            return new ItemStack(Material.PLAYER_HEAD);
        }
    }

    public ItemStack createItem(OfflinePlayer player)
    {
        return createItem(player.getUniqueId());
    }

    private String getApiResponse(String query) throws IOException
    {
        URL restRequest = new URL(query);
        InputStreamReader inputStreamReader = new InputStreamReader(restRequest.openStream());
        BufferedReader reader = new BufferedReader((inputStreamReader));
        StringBuilder responseBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null)
        {
            responseBuilder.append(line);
        }
        return responseBuilder.toString();
    }

    protected ItemMeta assignHeadUUIDToItem(ItemMeta meta, String uuid)
    {
        PersistentDataContainer dataHolder = meta.getPersistentDataContainer();
        NamespacedKey uuidKey = getSkullUUIDKey();
        dataHolder.set(uuidKey, PersistentDataType.STRING, uuid);
        return meta;
    }
}

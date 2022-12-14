package com.zazsona.decorheads.headdata;

import com.google.gson.Gson;
import com.zazsona.decorheads.Core;
import com.zazsona.decorheads.DecorHeadsUtil;
import com.zazsona.decorheads.apiresponse.ProfileResponse;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;

public class PlayerHead extends Head
{
    public static final String PLAYER_NAME_PLACEHOLDER = "%PlayerName%";
    public static final String PLAYER_SKIN_PLACEHOLDER = "%PlayerSkin%";

    private final String headNameFormat;

    public PlayerHead(String key, String headNameFormat)
    {
        super(key);
        this.headNameFormat = headNameFormat;
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
    public String getName()
    {
        return headNameFormat;
    }

    @Override
    public String getTexture()
    {
        return PLAYER_SKIN_PLACEHOLDER;
    }

    @Override
    public ItemStack createItem()
    {
        boolean isSteve = new Random().nextBoolean();
        String name = (isSteve) ? "Steve" : "Alex";
        UUID id = (isSteve) ? UUID.fromString("c06f8906-4c8a-4911-9c29-ea1dbd1aab82") : UUID.fromString("6ab43178-89fd-4905-97f6-0f67d9d76fd9");
        String headName = StringUtils.replaceIgnoreCase(headNameFormat, PLAYER_NAME_PLACEHOLDER, name);
        ItemStack item = createItem(id);
        item.getItemMeta().setDisplayName(headName);
        return item;
    }

    public ItemStack createItem(String playerName) throws IllegalArgumentException
    {
        try
        {
            String uuid = DecorHeadsUtil.fetchPlayerUUID(playerName);
            return createItem(UUID.fromString(uuid));
        }
        catch (IOException | NullPointerException e)
        {
            throw new IllegalArgumentException(String.format("Unable to get UUID for player: %s", playerName));
        }
    }

    public ItemStack createItem(OfflinePlayer player) throws IllegalArgumentException
    {
        return createItem(player.getUniqueId());
    }

    public ItemStack createItem(UUID uuid) throws IllegalArgumentException
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
            String skullName = StringUtils.replaceIgnoreCase(headNameFormat, PLAYER_NAME_PLACEHOLDER, pr.getName());
            ItemStack skull = createSkull(skullName, encodedSkullTexture);
            ItemMeta meta = applyDecorHeadsPlayerSkullMeta(skull.getItemMeta(), uuid.toString(), encodedSkullTexture);
            skull.setItemMeta(meta);
            return skull;
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException(String.format("Unable to resolve player head for UUID: %s", uuid));
        }
    }

    public ItemStack createItem(UUID uuid, String texture) throws IllegalArgumentException
    {
        try
        {
            ProfileResponse pr = DecorHeadsUtil.fetchPlayerProfile(uuid);
            String skullName = StringUtils.replaceIgnoreCase(headNameFormat, PLAYER_NAME_PLACEHOLDER, pr.getName());
            ItemStack skull = createSkull(skullName, texture);
            ItemMeta meta = applyDecorHeadsPlayerSkullMeta(skull.getItemMeta(), uuid.toString(), texture);
            skull.setItemMeta(meta);
            return skull;
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException(String.format("Unable to resolve player head for UUID: %s", uuid));
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

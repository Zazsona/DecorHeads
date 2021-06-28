package com.zazsona.decorheads.headdata;

import com.google.gson.Gson;
import com.zazsona.decorheads.Core;
import com.zazsona.decorheads.DecorHeadsUtil;
import com.zazsona.decorheads.apiresponse.NameUUIDResponse;
import com.zazsona.decorheads.apiresponse.ProfileResponse;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;

public class PlayerHead extends Head
{
    private static final String HEAD_NAME_FORMAT = "%s's Head";
    private String key;

    public PlayerHead(String key)
    {
        this.key = key;
    }

    @Override
    public String getKey()
    {
        return key;
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
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        if (player.hasPlayedBefore())
            return createItem(player);     //Create a Player Head (dynamically changes skin)
        else
        {           //Unfortunately, I'm yet to find a way to do dynamically changing heads for players that have never joined before, if it's even possible! As such, we have to generate a custom one.
            try
            {
                ProfileResponse pr = DecorHeadsUtil.fetchPlayerProfile(uuid);
                return createSkull(String.format(HEAD_NAME_FORMAT, pr.getName()), pr.getPropertyByName(TEXTURES_KEY).getValue());
            }
            catch (IOException e)
            {
                Bukkit.getLogger().warning(String.format("[%s] Unable to resolve player head for UUID: %s", Core.PLUGIN_NAME, uuid));
                return new ItemStack(Material.PLAYER_HEAD);
            }
        }
    }

    public ItemStack createItem(OfflinePlayer player)
    {
        return createSkull(String.format(HEAD_NAME_FORMAT, player.getName()), player);
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
}

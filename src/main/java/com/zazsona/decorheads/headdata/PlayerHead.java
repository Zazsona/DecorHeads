package com.zazsona.decorheads.headdata;

import com.google.gson.Gson;
import com.zazsona.decorheads.Core;
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
            String response = getApiResponse("https://api.mojang.com/users/profiles/minecraft/"+playerName);
            Gson gson = new Gson();
            NameUUIDResponse uuidResponse = gson.fromJson(response, NameUUIDResponse.class);
            if (uuidResponse.isSuccess())
            {
                String uuid = uuidResponse.getId().replaceFirst( "([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)", "$1-$2-$3-$4-$5" );
                return createItem(UUID.fromString(uuid));
            }
            else
                throw new IOException();
        }
        catch (IOException | NullPointerException e)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to get UUID for player: %s", Core.PLUGIN_NAME, playerName));
            e.printStackTrace();
        }
        return new ItemStack(Material.PLAYER_HEAD);
    }

    public ItemStack createItem(UUID uuid)
    {
        try
        {
            String response = getApiResponse("https://sessionserver.mojang.com/session/minecraft/profile/"+uuid);
            Gson gson = new Gson();
            ProfileResponse pr = gson.fromJson(response, ProfileResponse.class);
            if (pr.isSuccess())
                return createSkull(String.format("%s's Head", pr.getName()), pr.getPropertyByName(TEXTURES_KEY).getValue());
            else
                throw new IOException();
        }
        catch (IOException e)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to resolve player head for UUID: %s", Core.PLUGIN_NAME, uuid));
            return new ItemStack(Material.PLAYER_HEAD);
        }
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

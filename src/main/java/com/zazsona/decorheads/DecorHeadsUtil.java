package com.zazsona.decorheads;

import com.google.gson.Gson;
import com.zazsona.decorheads.apiresponse.NameUUIDResponse;
import com.zazsona.decorheads.apiresponse.ProfileResponse;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Locale;
import java.util.UUID;

public class DecorHeadsUtil
{
    public static String capitaliseName(String name)
    {
        name = name.replace("_", " ");
        String[] words = name.split(" ");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words.length; i++)
        {
            String word = words[i].toLowerCase();
            builder.append(word.substring(0, 1).toUpperCase());
            builder.append(word.substring(1));
            builder.append(" ");
        }
        return builder.toString().trim();
    }

    public static ProfileResponse fetchPlayerProfile(UUID uuid) throws IOException
    {
        String response = getApiResponse("https://sessionserver.mojang.com/session/minecraft/profile/"+uuid.toString());
        Gson gson = new Gson();
        ProfileResponse pr = gson.fromJson(response, ProfileResponse.class);
        if (pr.isSuccess())
            return pr;
        else
            throw new IOException(String.format("Profile returned error: %s", pr.getError()));
    }

    public static String fetchPlayerUUID(String playerName) throws IOException
    {
        String response = getApiResponse("https://api.mojang.com/users/profiles/minecraft/"+playerName);
        Gson gson = new Gson();
        NameUUIDResponse uuidResponse = gson.fromJson(response, NameUUIDResponse.class);
        if (uuidResponse.isSuccess())
        {
            String uuid = uuidResponse.getId().replaceFirst( "([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)", "$1-$2-$3-$4-$5" );
            return uuid;
        }
        else
            throw new IOException(String.format("Request returned error: %s", uuidResponse.getError()));
    }

    public static String getPlayerName(UUID uuid)
    {
        try
        {
            OfflinePlayer knownPlayer = Bukkit.getOfflinePlayer(uuid);
            if (!knownPlayer.hasPlayedBefore())
            {
                ProfileResponse profile = fetchPlayerProfile(uuid);
                return profile.getName();
            }
            else
            {
                return knownPlayer.getName();
            }
        }
        catch (IOException e)
        {
            return null;
        }
    }

    private static String getApiResponse(String query) throws IOException
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

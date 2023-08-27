package com.zazsona.decorheads.api;

import com.google.gson.Gson;
import com.zazsona.decorheads.api.response.NameUUIDResponse;
import com.zazsona.decorheads.api.response.ProfileResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.IOException;
import java.util.UUID;

public class PlayerProfileAPI extends APIBase
{
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

    public static String fetchPlayerName(UUID uuid)
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
}

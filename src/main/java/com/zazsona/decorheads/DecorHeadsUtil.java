package com.zazsona.decorheads;

import com.google.gson.Gson;
import com.zazsona.decorheads.apiresponse.NameUUIDResponse;
import com.zazsona.decorheads.apiresponse.ProfileResponse;
import com.zazsona.decorheads.blockmeta.BlockMetaKeys;
import com.zazsona.decorheads.blockmeta.BlockMetaRegionData;
import com.zazsona.decorheads.blockmeta.BlockMetaRepository;
import com.zazsona.decorheads.config.HeadRepository;
import com.zazsona.decorheads.headdata.IHead;
import com.zazsona.decorheads.headdata.PlayerHead;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Pattern;

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

    /**
     * Retrieves the head registration from a placed head block
     * @param block the head block
     * @return the head registration, or null if the block is not a valid or recognised head
     * @throws IOException unable to access head storage
     */
    public static IHead getBlockHeadData(Block block) throws IOException
    {
        if (block.getType() != Material.PLAYER_HEAD && block.getType() != Material.PLAYER_WALL_HEAD)
            return null;

        BlockMetaRepository metaRepository = BlockMetaRepository.getInstance();
        BlockMetaRegionData regionData = metaRepository.getRegionData(block.getChunk());
        HashMap<String, String> blockMeta = regionData.getBlockMeta(block.getLocation());
        if (!blockMeta.containsKey(BlockMetaKeys.HEAD_ID_KEY) && !DecorHeadsPlugin.getInstanceConfig().isHeadMetaPatcherEnabled())
            return null;

        if (blockMeta.containsKey(BlockMetaKeys.HEAD_ID_KEY))
        {
            String headKey = blockMeta.get(BlockMetaKeys.HEAD_ID_KEY);
            IHead head = HeadRepository.getLoadedHeads().get(headKey);
            return head;
        }

        if (!blockMeta.containsKey(BlockMetaKeys.HEAD_ID_KEY) && DecorHeadsPlugin.getInstanceConfig().isHeadMetaPatcherEnabled())
            return identifyUnknownHead(block);

        return null;
    }

    /**
     * Gets a head stack matching the head block
     * @param headBlock a head block
     * @return an ItemStack matching the block's data, or null if the block is not a valid head
     * @throws IOException unable to get player head details
     */
    @SuppressWarnings("deprecation") // Skull::getOwner() usage is required here, as the head name is custom. It does not belong to a player.
    public static ItemStack getHeadDrop(Block headBlock) throws IOException
    {
        IHead head = getBlockHeadData(headBlock);
        if (head == null)
            return null;

        if (head instanceof PlayerHead)
        {
            Location location = headBlock.getLocation();
            BlockMetaRepository metaRepository = BlockMetaRepository.getInstance();
            BlockMetaRegionData regionData = metaRepository.getRegionData(location.getChunk());
            HashMap<String, String> blockMeta = regionData.getBlockMeta(location);

            PlayerHead playerHead = (PlayerHead) head;
            String uuid = blockMeta.get(BlockMetaKeys.PLAYER_ID_KEY);
            String texture = blockMeta.get(BlockMetaKeys.HEAD_TEXTURE_KEY);
            if (uuid == null && DecorHeadsPlugin.getInstanceConfig().isHeadMetaPatcherEnabled())
            {
                Skull headBlockSkullState = (Skull) headBlock.getState();
                String instanceName = headBlockSkullState.getOwner();
                String playerName = extractPlayerNameFromHead(instanceName, head.getName());
                uuid = fetchPlayerUUID(playerName);
            }

            if (uuid != null && texture != null)
                return playerHead.createItem(UUID.fromString(uuid), texture);
            else if (uuid != null)
                return playerHead.createItem(UUID.fromString(uuid));
            else
                return playerHead.createItem();
        }
        else
            return head.createItem();
    }

    /**
     * Removes all head metadata from this location
     * @param location the location to purge
     * @throws IOException unable to get metadata file
     */
    public static void clearBlockHeadMeta(Location location) throws IOException
    {
        BlockMetaRepository repository = BlockMetaRepository.getInstance();
        BlockMetaRegionData regionData = repository.getRegionData(location.getChunk());
        regionData.removeBlockMeta(location, BlockMetaKeys.HEAD_ID_KEY);
        regionData.removeBlockMeta(location, BlockMetaKeys.PLAYER_ID_KEY);
        regionData.removeBlockMeta(location, BlockMetaKeys.HEAD_TEXTURE_KEY);
    }

    /**
     * Attempts to identify a DecorHeads head if the data has been lost
     * @param block the head block
     * @return the "best guess" head, or null if no reasonable identification could be made.
     */
    @SuppressWarnings("deprecation") // Skull::getOwner() usage is required here, as the head name is custom. It does not belong to a player.
    public static IHead identifyUnknownHead(Block block)
    {
        if (block.getBlockData().getMaterial() != Material.PLAYER_HEAD && block.getBlockData().getMaterial() != Material.PLAYER_WALL_HEAD)
            return null;

        Skull blockSkull = (Skull) block.getState();
        String instanceName = blockSkull.getOwner();
        return HeadRepository.matchLoadedHead(instanceName);
    }

    /**
     * Attempts to extract a Player's Name from a head name, using the provided template
     * @param headInstanceName - The name of a head, e.g "Zazsona's Head"
     * @param headNameTemplate - The template for a head's name, e.g "%PlayerName%'s Head"
     * @return the player's name, or null if no valid extract could be found.
     */
    public static String extractPlayerNameFromHead(String headInstanceName, String headNameTemplate)
    {
        try
        {
            String playerName = null;
            String headTemplateLwr = headNameTemplate.toLowerCase();
            String playerNamePlaceholderLwr = PlayerHead.PLAYER_NAME_PLACEHOLDER.toLowerCase();
            if (headTemplateLwr.contains(playerNamePlaceholderLwr))
            {
                int startIndex = headTemplateLwr.indexOf(playerNamePlaceholderLwr);
                char endChar = headNameTemplate.charAt(startIndex + playerNamePlaceholderLwr.length()); // Using "headNameTemplate" here to get the case-matching terminating char
                playerName = headInstanceName.toLowerCase().substring(startIndex).substring(0, headInstanceName.indexOf(endChar));
                if (Pattern.matches("[a-zA-Z0-9_]{2,16}", playerName))
                    return playerName;
            }
            return playerName;
        }
        catch (IndexOutOfBoundsException e)
        {
            // Head Instance's Name does not comply with the Head Name Template; name cannot be extracted.
            return null;
        }
    }
}

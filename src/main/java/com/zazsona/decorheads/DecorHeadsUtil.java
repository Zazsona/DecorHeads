package com.zazsona.decorheads;

import com.zazsona.decorheads.blockmeta.DecorHeadsBlockPluginPropertyKey;
import com.zazsona.decorheads.config.HeadRepository;
import com.zazsona.decorheads.headdata.IHead;
import com.zazsona.decorheads.headdata.PlayerHead;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class DecorHeadsUtil
{
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

        BlockPluginPropertyRepository metaRepository = BlockPluginPropertyRepository.getInstance();
        RegionBlockPluginPropertiesContainer regionData = metaRepository.getRegionData(block.getChunk());
        HashMap<String, String> blockMeta = regionData.getBlockPluginProperties(block.getLocation());
        if (!blockMeta.containsKey(DecorHeadsBlockPluginPropertyKey.HEAD_ID_KEY) && !DecorHeadsPlugin.getInstanceConfig().isHeadMetaPatcherEnabled())
            return null;

        if (blockMeta.containsKey(DecorHeadsBlockPluginPropertyKey.HEAD_ID_KEY))
        {
            String headKey = blockMeta.get(DecorHeadsBlockPluginPropertyKey.HEAD_ID_KEY);
            IHead head = HeadRepository.getLoadedHeads().get(headKey);
            return head;
        }

        if (!blockMeta.containsKey(DecorHeadsBlockPluginPropertyKey.HEAD_ID_KEY) && DecorHeadsPlugin.getInstanceConfig().isHeadMetaPatcherEnabled())
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
            BlockPluginPropertyRepository metaRepository = BlockPluginPropertyRepository.getInstance();
            RegionBlockPluginPropertiesContainer regionData = metaRepository.getRegionData(location.getChunk());
            HashMap<String, String> blockMeta = regionData.getBlockPluginProperties(location);

            PlayerHead playerHead = (PlayerHead) head;
            String uuid = blockMeta.get(DecorHeadsBlockPluginPropertyKey.PLAYER_ID_KEY);
            String texture = blockMeta.get(DecorHeadsBlockPluginPropertyKey.HEAD_TEXTURE_KEY);
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
}

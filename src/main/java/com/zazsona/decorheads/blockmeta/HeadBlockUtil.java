package com.zazsona.decorheads.blockmeta;

import com.zazsona.decorheads.DecorHeadsPlugin;
import com.zazsona.decorheads.api.PlayerProfileAPI;
import com.zazsona.decorheads.blockmeta.library.node.IBlockPluginPropertiesNode;
import com.zazsona.decorheads.config.HeadRepository;
import com.zazsona.decorheads.headdata.IHead;
import com.zazsona.decorheads.headdata.PlayerHead;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class HeadBlockUtil
{
    public static final String HEAD_ID_KEY = "headId";
    public static final String PLAYER_ID_KEY = "playerId";
    public static final String HEAD_TEXTURE_KEY = "headTexture";

    /**
     * Retrieves the head registration from a placed head block
     * @param block the head block
     * @return the head registration, or null if the block is not a valid or recognised head
     */
    public static IHead getHead(IBlockPluginPropertiesNode properties, Block block)
    {
        boolean headPatcherEnabled = DecorHeadsPlugin.getInstanceConfig().isHeadMetaPatcherEnabled();
        if (block.getType() != Material.PLAYER_HEAD && block.getType() != Material.PLAYER_WALL_HEAD)
            return null;

        String headId = properties.getBlockProperty(block.getLocation(), HEAD_ID_KEY);
        if (headId != null)
            return HeadRepository.getLoadedHeads().get(headId);

        if (!headPatcherEnabled)
            return null;

        Skull blockSkull = (Skull) block.getState();
        String instanceName = blockSkull.getOwner();
        return HeadRepository.matchLoadedHead(instanceName);
    }

    /**
     * Gets a head stack matching the head block
     * @param block a head block
     * @return an ItemStack matching the block's data, or null if the block is not a valid head
     */
    @SuppressWarnings("deprecation") // Skull::getOwner() usage is required here, as the head name is custom. It does not belong to a player.
    public static ItemStack getHeadDrop(IBlockPluginPropertiesNode properties, Block block)
    {
        IHead head = getHead(properties, block);
        if (head == null)
            return null;

        if (!(head instanceof PlayerHead))
            return head.createItem();

        PlayerHead playerHead = (PlayerHead) head;
        Map<String, String> headPlayerProperties = properties.getBlockProperties(block.getLocation(), PLAYER_ID_KEY, HEAD_TEXTURE_KEY);
        String uuid = headPlayerProperties.get(PLAYER_ID_KEY);
        String texture = headPlayerProperties.get(HEAD_TEXTURE_KEY);
        if (uuid == null && DecorHeadsPlugin.getInstanceConfig().isHeadMetaPatcherEnabled())
        {
            try
            {
                Skull headBlockSkullState = (Skull) block.getState();
                String instanceName = headBlockSkullState.getOwner();
                String playerName = playerHead.getPlayerNameFromHead(instanceName);
                uuid = PlayerProfileAPI.fetchPlayerUUID(playerName);
            }
            catch (IOException e)
            {
                DecorHeadsPlugin.getInstance().getLogger().warning("Unable to reach Player Profile API when patching head meta: " + e.getMessage());
                e.printStackTrace();
            }
        }

        if (uuid != null && texture != null)
            return playerHead.createItem(UUID.fromString(uuid), texture);
        else if (uuid != null)
            return playerHead.createItem(UUID.fromString(uuid));
        else
            return playerHead.createItem();
    }
}

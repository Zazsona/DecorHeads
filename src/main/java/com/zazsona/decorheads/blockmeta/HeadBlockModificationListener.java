package com.zazsona.decorheads.blockmeta;

import com.zazsona.decorheads.Core;
import com.zazsona.decorheads.DecorHeadsUtil;
import com.zazsona.decorheads.config.HeadRepository;
import com.zazsona.decorheads.config.PluginConfig;
import com.zazsona.decorheads.event.head.*;
import com.zazsona.decorheads.headdata.Head;
import com.zazsona.decorheads.headdata.IHead;
import com.zazsona.decorheads.headdata.PlayerHead;
import com.zazsona.decorheads.drops.drops.DropUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class HeadBlockModificationListener implements Listener
{
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockPlaced(BlockPlaceEvent e)
    {
        // Dependency checks
        ItemStack placedItem = e.getItemInHand();
        if (placedItem.getType() != Material.PLAYER_HEAD && placedItem.getType() != Material.PLAYER_WALL_HEAD)
            return;

        PersistentDataContainer dataContainer = placedItem.getItemMeta().getPersistentDataContainer();
        if (!dataContainer.has(Head.getSkullHeadKeyKey(), PersistentDataType.STRING))
            return;

        String headKey = dataContainer.get(Head.getSkullHeadKeyKey(), PersistentDataType.STRING);
        IHead head = HeadRepository.getLoadedHeads().get(headKey);

        HeadPlaceEvent headEvent = new HeadPlaceEvent(e.getBlockPlaced(), e.getBlockReplacedState(), e.getBlockAgainst(), e.getItemInHand(), e.getPlayer(), e.canBuild(), e.getHand(), head);
        Bukkit.getPluginManager().callEvent(headEvent);
        e.setCancelled(headEvent.isCancelled());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onHeadPlaced(HeadPlaceEvent e)
    {
        try
        {
            // Initialise variables
            Block block = e.getBlockPlaced();
            Location location = block.getLocation();
            ItemStack placedItem = e.getItemInHand();
            PersistentDataContainer dataContainer = placedItem.getItemMeta().getPersistentDataContainer();
            BlockMetaRepository metaRepository = BlockMetaRepository.getInstance();
            BlockMetaChunkData chunkMeta = metaRepository.getChunk(block.getChunk());

            // Write head id
            chunkMeta.addBlockMeta(location, BlockMetaKeys.HEAD_ID_KEY, e.getHead().getKey());

            // Write player id & texture
            if (dataContainer.has(PlayerHead.getSkullUUIDKey(), PersistentDataType.STRING))
            {
                String uuid = dataContainer.get(PlayerHead.getSkullUUIDKey(), PersistentDataType.STRING);
                chunkMeta.addBlockMeta(location, BlockMetaKeys.PLAYER_ID_KEY, uuid);
                if (dataContainer.has(PlayerHead.getSkullTextureKey(), PersistentDataType.STRING))
                {
                    String texture = dataContainer.get(PlayerHead.getSkullTextureKey(), PersistentDataType.STRING);
                    chunkMeta.addBlockMeta(location, BlockMetaKeys.HEAD_TEXTURE_KEY, texture);
                }
            }
        }
        catch (IOException ioEx)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to load chunk meta: %s", Core.PLUGIN_NAME, ioEx.getMessage()));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e)
    {
        try
        {
            Block block = e.getBlock();
            IHead head = getBlockHeadData(block);
            if (head != null)
            {
                HeadBreakEvent headEvent = new HeadBreakEvent(e.getBlock(), e.getPlayer(), head);
                Bukkit.getPluginManager().callEvent(headEvent);
                e.setCancelled(headEvent.isCancelled());
            }
        }
        catch (IOException ioEx)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to load chunk meta: %s", Core.PLUGIN_NAME, ioEx.getMessage()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onHeadBreak(HeadBreakEvent e)
    {
        try
        {
            Block block = e.getBlock();
            if (e.getPlayer().getGameMode() != GameMode.CREATIVE)
            {
                ItemStack drop = getHeadDrop(block);
                DropUtil.dropItem(block.getLocation(), drop);
            }
            e.setDropItems(false);
            clearBlockHeadMeta(block.getLocation());
        }
        catch (IOException ioEx)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to load chunk meta: %s", Core.PLUGIN_NAME, ioEx.getMessage()));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent e)
    {
        try
        {
            IHead explodedHead = getBlockHeadData(e.getBlock());
            if (explodedHead != null)
            {
                HeadExplodeEvent headEvent = new HeadExplodeEvent(e.getBlock(), e.blockList(), e.getYield(), explodedHead);
                Bukkit.getPluginManager().callEvent(headEvent);
                e.setCancelled(headEvent.isCancelled());
                if (e.isCancelled())
                    return;
            }

            Iterator blockIterator = e.blockList().iterator();
            while (blockIterator.hasNext())
            {
                Block block = (Block) blockIterator.next();
                IHead head = getBlockHeadData(block);
                if (head != null)
                {
                    HeadBreakByExplosionEvent headEvent = new HeadBreakByExplosionEvent(block, e.getBlock(), e.blockList(), e.getYield(), head);
                    Bukkit.getPluginManager().callEvent(headEvent);
                    if (headEvent.isCancelled())
                        blockIterator.remove();
                }
            }
        }
        catch (IOException ioEx)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to load chunk meta: %s", Core.PLUGIN_NAME, ioEx.getMessage()));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent e)
    {
        try
        {
            Iterator blockIterator = e.blockList().iterator();
            while (blockIterator.hasNext())
            {
                Block block = (Block) blockIterator.next();
                IHead head = getBlockHeadData(block);
                if (head != null)
                {
                    HeadBreakByExplosionEvent headEvent = new HeadBreakByExplosionEvent(block, e.getEntity(), e.blockList(), e.getYield(), head);
                    Bukkit.getPluginManager().callEvent(headEvent);
                    if (headEvent.isCancelled())
                        blockIterator.remove();
                }
            }
        }
        catch (IOException ioEx)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to load chunk meta: %s", Core.PLUGIN_NAME, ioEx.getMessage()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onHeadBreakByExplosion(HeadBreakByExplosionEvent e)
    {
        try
        {
            Block block = e.getBlock();
            if (DropUtil.rollDrop(e.getExplosionYield()))
            {
                ItemStack drop = getHeadDrop(block);
                DropUtil.dropItem(block.getLocation(), drop);
            }
            clearBlockHeadMeta(block.getLocation());

            Iterator<Block> iterator = e.getBlocks().iterator();
            while (iterator.hasNext())
            {
                Block iteratedBlock = iterator.next();
                if (iteratedBlock == block)
                {
                    iterator.remove();
                    block.setType(Material.AIR);    // "Blow up" the block.
                    return;
                }
            }
        }
        catch (IOException ioEx)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to load chunk meta: %s", Core.PLUGIN_NAME, ioEx.getMessage()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPistonExtend(BlockPistonExtendEvent e)
    {
        try
        {
            for (Block movingBlock : e.getBlocks())
            {
                ItemStack dropStack = getHeadDrop(movingBlock);
                if (dropStack != null)
                {
                    DropUtil.dropItem(movingBlock.getLocation(), dropStack);
                    clearBlockHeadMeta(movingBlock.getLocation());
                    movingBlock.setType(Material.AIR);
                }
            }
        }
        catch (IOException ioEx)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to load chunk meta: %s", Core.PLUGIN_NAME, ioEx.getMessage()));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockBurn(BlockBurnEvent e)
    {
        try
        {
            Block block = e.getBlock();
            IHead head = getBlockHeadData(block);
            if (head != null)
            {
                HeadBurnEvent headEvent = new HeadBurnEvent(block, e.getIgnitingBlock(), head);
                Bukkit.getPluginManager().callEvent(headEvent);
                e.setCancelled(headEvent.isCancelled());
            }
        }
        catch (IOException ioEx)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to load chunk meta: %s", Core.PLUGIN_NAME, ioEx.getMessage()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onHeadBurn(HeadBurnEvent e)
    {
        try
        {
            Block block = e.getBlock();
            ItemStack dropStack = getHeadDrop(block);
            if (dropStack != null)
            {
                DropUtil.dropItem(block.getLocation(), dropStack);
                clearBlockHeadMeta(block.getLocation());
                e.setCancelled(true);
                block.setType(Material.AIR);
            }
        }
        catch (IOException ioEx)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to load chunk meta: %s", Core.PLUGIN_NAME, ioEx.getMessage()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFromTo(BlockFromToEvent e)
    {
        try
        {
            Block block = e.getBlock();
            ItemStack dropStack = getHeadDrop(block);
            if (dropStack != null)
            {
                DropUtil.dropItem(block.getLocation(), dropStack);
                clearBlockHeadMeta(block.getLocation());
                e.setCancelled(true);
                block.setType(Material.AIR);
            }
        }
        catch (IOException ioEx)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to load chunk meta: %s", Core.PLUGIN_NAME, ioEx.getMessage()));
        }
    }

    /**
     * Retrieves the head registration from a placed head block
     * @param block the head block
     * @return the head registration, or null if the block is not a valid or recognised head
     * @throws IOException unable to access head storage
     */
    private IHead getBlockHeadData(Block block) throws IOException
    {
        if (block.getType() != Material.PLAYER_HEAD && block.getType() != Material.PLAYER_WALL_HEAD)
            return null;

        BlockMetaRepository metaRepository = BlockMetaRepository.getInstance();
        BlockMetaChunkData chunkMeta = metaRepository.getChunk(block.getChunk());
        HashMap<String, String> blockMeta = chunkMeta.getBlockMeta(block.getLocation());
        if (!blockMeta.containsKey(BlockMetaKeys.HEAD_ID_KEY) && !PluginConfig.isHeadMetaPatcherEnabled())
            return null;

        if (blockMeta.containsKey(BlockMetaKeys.HEAD_ID_KEY))
        {
            String headKey = blockMeta.get(BlockMetaKeys.HEAD_ID_KEY);
            IHead head = HeadRepository.getLoadedHeads().get(headKey);
            return head;
        }

        if (!blockMeta.containsKey(BlockMetaKeys.HEAD_ID_KEY) && PluginConfig.isHeadMetaPatcherEnabled())
            return identifyUnknownHead(block);

        return null;
    }

    /**
     * Attempts to identify a DecorHeads head if the data has been lost
     * @param block the head block
     * @return the "best guess" head, or null if no reasonable identification could be made.
     */
    @SuppressWarnings("deprecation") // Skull::getOwner() usage is required here, as the head name is custom. It does not belong to a player.
    private IHead identifyUnknownHead(Block block)
    {
        if (block.getBlockData().getMaterial() != Material.PLAYER_HEAD && block.getBlockData().getMaterial() != Material.PLAYER_WALL_HEAD)
            return null;

        Skull blockSkull = (Skull) block.getState();
        String instanceName = blockSkull.getOwner();
        return HeadRepository.matchLoadedHead(instanceName);
    }

    /**
     * Gets a head stack matching the head block
     * @param headBlock a head block
     * @return an ItemStack matching the block's data, or null if the block is not a valid head
     * @throws IOException unable to get player head details
     */
    @SuppressWarnings("deprecation") // Skull::getOwner() usage is required here, as the head name is custom. It does not belong to a player.
    private ItemStack getHeadDrop(Block headBlock) throws IOException
    {
        IHead head = getBlockHeadData(headBlock);
        if (head == null)
            return null;

        if (head instanceof PlayerHead)
        {
            Location location = headBlock.getLocation();
            BlockMetaRepository metaRepository = BlockMetaRepository.getInstance();
            BlockMetaChunkData chunkMeta = metaRepository.getChunk(location.getChunk());
            HashMap<String, String> blockMeta = chunkMeta.getBlockMeta(location);

            PlayerHead playerHead = (PlayerHead) head;
            String uuid = blockMeta.get(BlockMetaKeys.PLAYER_ID_KEY);
            String texture = blockMeta.get(BlockMetaKeys.HEAD_TEXTURE_KEY);
            if (uuid == null && PluginConfig.isHeadMetaPatcherEnabled())
            {
                Skull headBlockSkullState = (Skull) headBlock.getState();
                String instanceName = headBlockSkullState.getOwner();
                String playerName = DecorHeadsUtil.extractPlayerNameFromHead(instanceName, head.getName());
                uuid = DecorHeadsUtil.fetchPlayerUUID(playerName);
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
    private void clearBlockHeadMeta(Location location) throws IOException
    {
        BlockMetaRepository repository = BlockMetaRepository.getInstance();
        BlockMetaChunkData chunk = repository.getChunk(location.getChunk());
        chunk.removeBlockMeta(location, BlockMetaKeys.HEAD_ID_KEY);
        chunk.removeBlockMeta(location, BlockMetaKeys.PLAYER_ID_KEY);
        chunk.removeBlockMeta(location, BlockMetaKeys.HEAD_TEXTURE_KEY);
    }
}

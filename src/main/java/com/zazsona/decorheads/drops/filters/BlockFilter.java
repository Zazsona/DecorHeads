package com.zazsona.decorheads.drops.filters;

import com.zazsona.decorheads.DecorHeadsPlugin;
import com.zazsona.decorheads.blockmeta.BlockMetaChunkData;
import com.zazsona.decorheads.blockmeta.BlockMetaKeys;
import com.zazsona.decorheads.blockmeta.BlockMetaRepository;
import com.zazsona.decorheads.DropType;
import com.zazsona.decorheads.event.block.BlockBreakByExplosionEvent;
import com.zazsona.decorheads.event.block.BlockPistonReactionEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Checks if block-related events pass a query
 */
public class BlockFilter extends DropFilter
{
    private Set<NamespacedKey> blockKeys = new HashSet<>();

    /**
     * Creates a new {@link BlockFilter} where any block with the provided {@link NamespacedKey} will drop the result.
     * @param blockKey the key
     */
    public BlockFilter(NamespacedKey blockKey)
    {
        if (this.blockKeys != null)
            this.blockKeys.add(blockKey);
    }

    /**
     * Creates a new {@link BlockFilter} where any blocks with the provided {@link NamespacedKey}s will drop the result.
     * @param blockKeys the keys
     */
    public BlockFilter(Collection<NamespacedKey> blockKeys)
    {
        if (this.blockKeys != null)
            this.blockKeys.addAll(blockKeys);
    }

    /**
     * Gets the block {@link NamespacedKey}s
     * @return the keys
     */
    public Set<NamespacedKey> getBlockKeys()
    {
        return new HashSet<>(blockKeys);
    }

    /**
     * Checks if the provided block enables this filter to pass
     * @param block the block to check
     * @return boolean on filter passed
     */
    private boolean checkPass(Block block)
    {
        try
        {
            if (block == null)
                return false;
            if (blockKeys.size() == 0)
                return true;

            Material blockType = block.getType();
            NamespacedKey blockKey = blockType.getKey();
            if (blockType == Material.PLAYER_HEAD && blockType == Material.PLAYER_WALL_HEAD)
            {
                BlockMetaRepository metaRepo = BlockMetaRepository.getInstance();
                BlockMetaChunkData chunkMeta = metaRepo.getChunk(block.getChunk());
                HashMap<String, String> blockMeta = chunkMeta.getBlockMeta(block.getLocation());
                String headId = blockMeta.get(BlockMetaKeys.HEAD_ID_KEY);
                if (headId != null)
                {
                    NamespacedKey headKey = new NamespacedKey(DecorHeadsPlugin.getInstance(), headId);
                    return blockKeys.contains(headKey);
                }
            }
            return (blockKeys.contains(blockKey));
        }
        catch (IOException ex)
        {
            Bukkit.getLogger().warning(String.format("[%s] Block filter failed: %s", DecorHeadsPlugin.PLUGIN_NAME, ex.getMessage()));
            return false;
        }
    }

    @Override
    protected boolean isFilterPass(DropType dropType, BlockBreakEvent e)
    {
        Block block = e.getBlock();
        return checkPass(block);
    }

    @Override
    protected boolean isFilterPass(DropType dropType, EntityDeathEvent e)
    {
        LivingEntity entity = e.getEntity();
        Location location = entity.getLocation();
        location.setY(location.getY() - 1.0f); //Entity location returns the Y position at the entity's feet. To get the block beneath, simply -1!
        Block block = entity.getWorld().getBlockAt(location);
        return checkPass(block);
    }

    @Override
    protected boolean isFilterPass(DropType dropType, CraftItemEvent e)
    {
        Location location = e.getInventory().getLocation();
        Block block = location.getBlock();
        return checkPass(block);
    }

    @Override
    protected boolean isFilterPass(DropType dropType, BrewEvent e)
    {
        Block block = e.getBlock();
        return checkPass(block);
    }

    @Override
    protected boolean isFilterPass(DropType dropType, BlockCookEvent e)
    {
        Block block = e.getBlock();
        return checkPass(block);
    }

    @Override
    protected boolean isFilterPass(DropType dropType, BlockBreakByExplosionEvent e)
    {
        Block block = e.getBlock();
        return checkPass(block);
    }

    @Override
    protected boolean isFilterPass(DropType dropType, BlockPistonReactionEvent e)
    {
        Block block = e.getBlock();
        return checkPass(block);
    }

    @Override
    protected boolean isFilterPass(DropType dropType, BlockExplodeEvent e)
    {
        Block block = e.getBlock();
        return checkPass(block);
    }

    @Override
    protected boolean isFilterPass(DropType dropType, BlockPistonExtendEvent e)
    {
        Block block = e.getBlock();
        return checkPass(block);
    }

    @Override
    protected boolean isFilterPass(DropType dropType, BlockBurnEvent e)
    {
        Block block = e.getBlock();
        return checkPass(block);
    }

    @Override
    protected boolean isFilterPass(DropType dropType, BlockFromToEvent e)
    {
        Block block = e.getBlock();
        return checkPass(block);
    }
}

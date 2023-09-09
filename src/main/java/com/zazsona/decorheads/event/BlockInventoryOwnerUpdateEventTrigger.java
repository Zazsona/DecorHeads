package com.zazsona.decorheads.event;

import com.zazsona.decorheads.MaterialUtil;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Campfire;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class BlockInventoryOwnerUpdateEventTrigger implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent e)
    {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK || e.getClickedBlock() != null)
            return;

        Player player = e.getPlayer();
        Block clickedBlock = e.getClickedBlock();
        if (!(clickedBlock.getState() instanceof Campfire))
            return;
        ItemStack usedItem = e.getItem();
        if (usedItem == null || !MaterialUtil.isCookableFood(usedItem.getType()))
            return;

        BlockInventoryOwnerUpdateEvent event = new BlockInventoryOwnerUpdateEvent(clickedBlock, player.getUniqueId());
        Bukkit.getPluginManager().callEvent(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryUsed(InventoryClickEvent e)
    {
        if (e == null || e.getInventory() == null)
            return;

        InventoryHolder inventoryHolder = e.getInventory().getHolder();
        if (!(inventoryHolder instanceof BlockState))
            return;

        BlockState blockState = (BlockState) inventoryHolder;
        Block block = blockState.getBlock();
        Player player = (Player) e.getWhoClicked();

        BlockInventoryOwnerUpdateEvent event = new BlockInventoryOwnerUpdateEvent(block, player.getUniqueId());
        Bukkit.getPluginManager().callEvent(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e)
    {
        BlockInventoryOwnerUpdateEvent event = createOwnerRemovedEvent(e.getBlock());
        Bukkit.getPluginManager().callEvent(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent e)
    {
        Iterator blockIterator = e.blockList().iterator();
        while (blockIterator.hasNext())
        {
            Block block = (Block) blockIterator.next();
            BlockInventoryOwnerUpdateEvent event = createOwnerRemovedEvent(block);
            Bukkit.getPluginManager().callEvent(event);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent e)
    {
        Iterator blockIterator = e.blockList().iterator();
        while (blockIterator.hasNext())
        {
            Block block = (Block) blockIterator.next();
            BlockInventoryOwnerUpdateEvent event = createOwnerRemovedEvent(block);
            Bukkit.getPluginManager().callEvent(event);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPistonExtend(BlockPistonExtendEvent e)
    {
        for (Block movingBlock : e.getBlocks())
        {
            if (movingBlock.getPistonMoveReaction() == PistonMoveReaction.BLOCK)
                continue;
            if (movingBlock.getPistonMoveReaction() == PistonMoveReaction.IGNORE)
                continue;

            BlockInventoryOwnerUpdateEvent event = createOwnerRemovedEvent(movingBlock);
            Bukkit.getPluginManager().callEvent(event);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBurn(BlockBurnEvent e)
    {
        BlockInventoryOwnerUpdateEvent event = createOwnerRemovedEvent(e.getBlock());
        Bukkit.getPluginManager().callEvent(event);
    }

    /**
     * Creates a {@link BlockInventoryOwnerUpdateEvent} that indicated the owner data has been removed
     * @param block the inventory-owning block
     * @return the event
     */
    @NotNull
    private static BlockInventoryOwnerUpdateEvent createOwnerRemovedEvent(Block block)
    {
        BlockInventoryOwnerUpdateEvent event = new BlockInventoryOwnerUpdateEvent(block, null);
        return event;
    }
}

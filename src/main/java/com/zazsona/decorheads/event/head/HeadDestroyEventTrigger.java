package com.zazsona.decorheads.event.head;

import com.zazsona.decorheads.blockmeta.HeadBlockUtil;
import com.zazsona.decorheads.blockmeta.library.node.IMutableBlockPluginPropertiesNode;
import com.zazsona.decorheads.drops.drops.DropUtil;
import com.zazsona.decorheads.event.BlockBreakByExplosionEvent;
import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class HeadDestroyEventTrigger implements Listener
{
    protected IMutableBlockPluginPropertiesNode blockProperties;

    private HashMap<Location, HashMap<String, HeadDestroyEvent>> headDestroyEventByRootEventAtLocation;

    public HeadDestroyEventTrigger(IMutableBlockPluginPropertiesNode blockProperties)
    {
        this.blockProperties = blockProperties;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e)
    {
        Block block = e.getBlock();
        IHead head = HeadBlockUtil.getHead(blockProperties, block);
        if (head == null)
            return;

        HeadBreakEvent headBreakEvent = new HeadBreakEvent(block, head, e.getPlayer());
        Bukkit.getPluginManager().callEvent(headBreakEvent);
        mapHeadDestroyEvent(headBreakEvent, e);
        e.setCancelled(headBreakEvent.isCancelled()); // This handler only fires if the event isn't cancelled already, so this can't "uncancel"
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreakResult(BlockBreakEvent e)
    {
        HeadBreakEvent headBreakEvent = (HeadBreakEvent) removeHeadDestroyEvent(e);
        if (headBreakEvent == null)
            return;
        if (isEventSeriesCancelled(headBreakEvent, e))
            return;
        if (!headBreakEvent.isDropHead())
            return;

        Block block = headBreakEvent.getBlock();
        dropHead(block);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent e)
    {
        Block block = e.getBlock();
        IHead head = HeadBlockUtil.getHead(blockProperties, block);
        if (head == null)
            return;

        HeadDestroyEvent headDestroyEvent = new HeadDestroyEvent(block, head);
        Bukkit.getPluginManager().callEvent(headDestroyEvent);
        mapHeadDestroyEvent(headDestroyEvent, e);
        e.setCancelled(headDestroyEvent.isCancelled()); // This handler only fires if the event isn't cancelled already, so this can't "uncancel"
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockExplodeResult(BlockExplodeEvent e)
    {
        HeadDestroyEvent headDestroyEvent = removeHeadDestroyEvent(e);
        if (headDestroyEvent == null)
            return;
        if (isEventSeriesCancelled(headDestroyEvent, e))
            return;

        Block block = headDestroyEvent.getBlock();
        dropHead(block);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockBreakByExplosion(BlockBreakByExplosionEvent e)
    {
        Block block = e.getBlock();
        IHead head = HeadBlockUtil.getHead(blockProperties, block);
        if (head == null)
            return;

        HeadBreakByExplosionEvent headDestroyEvent = new HeadBreakByExplosionEvent(block, head, e.getExplosionSource(), e.getBlocks(), e.getExplosionYield());
        Bukkit.getPluginManager().callEvent(headDestroyEvent);
        mapHeadDestroyEvent(headDestroyEvent, e);
        e.setCancelled(headDestroyEvent.isCancelled()); // This handler only fires if the event isn't cancelled already, so this can't "uncancel"
    }

    @EventHandler(priority = EventPriority.HIGHEST) // Highest priority, as we may cancel the event so can't just monitor
    public void onBlockBreakByExplosionResult(BlockBreakByExplosionEvent e)
    {
        HeadBreakByExplosionEvent headBreakEvent = (HeadBreakByExplosionEvent) getHeadDestroyEvent(e);
        if (headBreakEvent == null)
            return;
        if (isEventSeriesCancelled(headBreakEvent, e))
            return;

        Block block = headBreakEvent.getBlock();
        Location blockLoc = block.getLocation();

        e.setCancelled(true); // Cancel the explosion; we're manually exploding it.
        block.setType(Material.AIR);
        // Don't call dropHeads, as we've changed the block type
        ItemStack dropStack = HeadBlockUtil.getHeadDrop(blockProperties, block);
        if (dropStack != null)
            DropUtil.dropItem(blockLoc, dropStack);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockBurn(BlockBurnEvent e)
    {
        Block block = e.getBlock();
        IHead head = HeadBlockUtil.getHead(blockProperties, block);
        if (head == null)
            return;

        HeadDestroyEvent headDestroyEvent = new HeadDestroyEvent(block, head);
        Bukkit.getPluginManager().callEvent(headDestroyEvent);
        mapHeadDestroyEvent(headDestroyEvent, e);
        e.setCancelled(headDestroyEvent.isCancelled()); // This handler only fires if the event isn't cancelled already, so this can't "uncancel"
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBurnResult(BlockBurnEvent e)
    {
        HeadDestroyEvent headDestroyEvent = removeHeadDestroyEvent(e);
        if (headDestroyEvent == null)
            return;
        if (isEventSeriesCancelled(headDestroyEvent, e))
            return;

        Block block = headDestroyEvent.getBlock();
        dropHead(block);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPistonExtend(BlockPistonExtendEvent e)
    {
        for (Block movingBlock : e.getBlocks())
        {
            IHead head = HeadBlockUtil.getHead(blockProperties, movingBlock);
            if (head == null)
                continue;
            HeadDestroyEvent headDestroyEvent = new HeadDestroyEvent(movingBlock, head);
            Bukkit.getPluginManager().callEvent(headDestroyEvent);
            mapHeadDestroyEvent(headDestroyEvent, e, movingBlock);
            if (headDestroyEvent.isCancelled())
                e.setCancelled(true); // There's no way to just cancel the head destruction; we have to kill the whole piston movement
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPistonExtendResult(BlockPistonExtendEvent e)
    {
        for (Block movingBlock : e.getBlocks())
        {
            HeadDestroyEvent headDestroyEvent = removeHeadDestroyEvent(e, movingBlock);
            if (headDestroyEvent == null)
                return;
            if (isEventSeriesCancelled(headDestroyEvent, e))
                return;

            dropHead(movingBlock);
        }
    }

    // Used for detecting when liquids (water, lava) overtake a head block
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onFromTo(BlockFromToEvent e)
    {
        Block block = e.getBlock();
        IHead head = HeadBlockUtil.getHead(blockProperties, block);
        if (head == null)
            return;

        HeadDestroyEvent headDestroyEvent = new HeadDestroyEvent(block, head);
        Bukkit.getPluginManager().callEvent(headDestroyEvent);
        mapHeadDestroyEvent(headDestroyEvent, e);
        e.setCancelled(headDestroyEvent.isCancelled()); // This handler only fires if the event isn't cancelled already, so this can't "uncancel"
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onFromToResult(BlockFromToEvent e)
    {
        HeadDestroyEvent headDestroyEvent = removeHeadDestroyEvent(e);
        if (headDestroyEvent == null)
            return;
        if (isEventSeriesCancelled(headDestroyEvent, e))
            return;

        Block block = headDestroyEvent.getBlock();
        dropHead(block);
    }

    /**
     * Drops a head for the provided block
     * @param block the head block
     */
    private void dropHead(Block block)
    {
        Location blockLoc = block.getLocation();
        ItemStack dropStack = HeadBlockUtil.getHeadDrop(blockProperties, block);
        if (dropStack != null)
            DropUtil.dropItem(blockLoc, dropStack);
    }

    /**
     * Maps the {@link HeadDestroyEvent} to its root cause on the block.
     * @param headDestroyEvent the event to trigger
     * @param rootEvent the causing event
     * @param block the head block
     * @return the final state of the event
     */
    @NotNull
    private HeadDestroyEvent mapHeadDestroyEvent(HeadDestroyEvent headDestroyEvent, BlockEvent rootEvent, Block block)
    {
        if (!headDestroyEvent.isCancelled())
        {
            Location blockLoc = block.getLocation();
            if (!headDestroyEventByRootEventAtLocation.containsKey(blockLoc))
                headDestroyEventByRootEventAtLocation.put(blockLoc, new HashMap<>());

            headDestroyEventByRootEventAtLocation.get(blockLoc).put(rootEvent.getEventName(), headDestroyEvent);
        }

        return headDestroyEvent;
    }

    /**
     * Maps the {@link HeadDestroyEvent} to its root cause on the block.
     * @param headDestroyEvent the event to trigger
     * @param rootEvent the causing event
     * @return the final state of the event
     */
    @NotNull
    private HeadDestroyEvent mapHeadDestroyEvent(HeadDestroyEvent headDestroyEvent, BlockEvent rootEvent)
    {
        return mapHeadDestroyEvent(headDestroyEvent, rootEvent, rootEvent.getBlock());
    }

    /**
     * Gets the mapped {@link HeadDestroyEvent} caused by the provided root event
     * @param rootEvent the root event, causing the head destruction
     * @return the {@link HeadDestroyEvent}, or null if there is no mapped event
     */
    private HeadDestroyEvent getHeadDestroyEvent(BlockEvent rootEvent)
    {
        return getHeadDestroyEvent(rootEvent, rootEvent.getBlock());
    }

    /**
     * Gets the mapped {@link HeadDestroyEvent} caused by the provided root event
     * @param rootEvent the root event, causing the head destruction
     * @param block the head block destroyed by the rootEvent
     * @return the {@link HeadDestroyEvent}, or null if there is no mapped event
     */
    private HeadDestroyEvent getHeadDestroyEvent(BlockEvent rootEvent, Block block)
    {
        Location blockLoc = block.getLocation();
        HashMap<String, HeadDestroyEvent> headDestroyEventsByRootEventName = headDestroyEventByRootEventAtLocation.get(blockLoc);
        if (headDestroyEventsByRootEventName == null || !headDestroyEventsByRootEventName.containsKey(rootEvent.getEventName()))
            return null;

        HeadDestroyEvent headDestroyEvent = headDestroyEventsByRootEventName.get(rootEvent.getEventName());
        return headDestroyEvent;
    }

    /**
     * Removes the mapped {@link HeadDestroyEvent} caused by the provided root event
     * @param rootEvent the root event, causing the head destruction
     * @return the {@link HeadDestroyEvent}, or null if there was no mapped event
     */
    private HeadDestroyEvent removeHeadDestroyEvent(BlockEvent rootEvent)
    {
        return removeHeadDestroyEvent(rootEvent, rootEvent.getBlock());
    }

    /**
     * Removes the mapped {@link HeadDestroyEvent} caused by the provided root event
     * @param rootEvent the root event, causing the head destruction
     * @param block the head block destroyed by the rootEvent
     * @return the {@link HeadDestroyEvent}, or null if there was no mapped event
     */
    private HeadDestroyEvent removeHeadDestroyEvent(BlockEvent rootEvent, Block block)
    {
        Location blockLoc = block.getLocation();
        HashMap<String, HeadDestroyEvent> headDestroyEventsByRootEventName = headDestroyEventByRootEventAtLocation.get(blockLoc);
        if (headDestroyEventsByRootEventName == null || !headDestroyEventsByRootEventName.containsKey(rootEvent.getEventName()))
            return null;

        HeadDestroyEvent headDestroyEvent = headDestroyEventsByRootEventName.remove(rootEvent.getEventName());
        if (headDestroyEventByRootEventAtLocation.get(blockLoc).isEmpty())
            headDestroyEventByRootEventAtLocation.remove(blockLoc);

        return headDestroyEvent;
    }

    /**
     * Returns whether this series of events is cancelled
     * @param headDestroyEvent the child event
     * @param rootEvent the parent event
     * @return true if cancelled; otherwise false
     */
    private static boolean isEventSeriesCancelled(HeadDestroyEvent headDestroyEvent, BlockEvent rootEvent)
    {
        boolean rootCancelled = (!(rootEvent instanceof Cancellable) || ((rootEvent instanceof Cancellable) && (!((Cancellable) rootEvent).isCancelled())));
        boolean childCancelled = (!(headDestroyEvent instanceof Cancellable) || ((headDestroyEvent instanceof Cancellable) && (!((Cancellable) headDestroyEvent).isCancelled())));
        return rootCancelled || childCancelled;
    }
}

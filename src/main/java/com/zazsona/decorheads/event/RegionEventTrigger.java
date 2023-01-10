package com.zazsona.decorheads.event;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class RegionEventTrigger implements Listener
{
    private HashMap<String, Set<Chunk>> regionLoadedChunksMap = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChunkLoad(ChunkLoadEvent e)
    {
        Chunk chunk = e.getChunk();
        String key = RegionEvent.getRegionKey(chunk);
        if (regionLoadedChunksMap.containsKey(key))
            return;

        regionLoadedChunksMap.put(key, new HashSet<>());
        regionLoadedChunksMap.get(key).add(chunk);

        RegionLoadEvent regionLoadEvent = new RegionLoadEvent(chunk);
        Bukkit.getPluginManager().callEvent(regionLoadEvent);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent e)
    {
        Chunk chunk = e.getChunk();
        String key = RegionEvent.getRegionKey(chunk);
        if (!regionLoadedChunksMap.containsKey(key))
            return;

        regionLoadedChunksMap.get(key).remove(chunk);
        if (regionLoadedChunksMap.get(key).size() > 0)
            return;

        regionLoadedChunksMap.remove(key);
        RegionUnloadEvent regionUnloadEvent = new RegionUnloadEvent(chunk);
        Bukkit.getPluginManager().callEvent(regionUnloadEvent);
    }
}

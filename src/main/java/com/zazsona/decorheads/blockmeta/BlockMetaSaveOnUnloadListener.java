package com.zazsona.decorheads.blockmeta;

import com.zazsona.decorheads.DecorHeadsPlugin;
import com.zazsona.decorheads.event.RegionEvent;
import com.zazsona.decorheads.event.RegionUnloadEvent;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.world.WorldSaveEvent;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlockMetaSaveOnUnloadListener implements Listener
{
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onRegionUnlock(RegionUnloadEvent e)
    {
        try
        {
            BlockMetaRegionData regionMeta = BlockMetaRepository.getInstance().getRegionData(e.getRegionKey(), e.getWorld().getName());
            regionMeta.save();
        }
        catch (IOException ioEx)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to save region meta: %s", DecorHeadsPlugin.PLUGIN_NAME, ioEx.getMessage()));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void OnWorldSave(WorldSaveEvent e)
    {
        BlockMetaRepository repo = BlockMetaRepository.getInstance();
        World world = e.getWorld();

        saveLoadedRegionsMeta(repo, world);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPluginDisable(PluginDisableEvent e)
    {
        if (e.getPlugin().getName().equals(DecorHeadsPlugin.getInstance().getName()))
        {
            BlockMetaRepository repo = BlockMetaRepository.getInstance();
            List<World> worlds = Bukkit.getWorlds();
            for (World world : worlds)
                saveLoadedRegionsMeta(repo, world);
        }
    }

    private void saveLoadedRegionsMeta(BlockMetaRepository repo, World world)
    {
        String worldName = world.getName();
        Chunk[] chunks = world.getLoadedChunks();
        Set<String> regionKeys = new HashSet<>();
        for (Chunk chunk : chunks)
        {
            String regionKey = RegionEvent.getRegionKey(chunk);
            regionKeys.add(regionKey);
        }

        regionKeys.forEach(regionKey ->
                           {
                               try
                               {
                                   repo.getRegionData(regionKey, worldName).save();
                               }
                               catch (IOException ioEx)
                               {
                                   Bukkit.getLogger().warning(String.format("[%s] Unable to save region meta: %s", DecorHeadsPlugin.PLUGIN_NAME, ioEx.getMessage()));
                               }
                           });
    }
}

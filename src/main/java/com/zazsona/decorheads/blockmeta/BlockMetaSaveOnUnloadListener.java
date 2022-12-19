package com.zazsona.decorheads.blockmeta;

import com.zazsona.decorheads.DecorHeadsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.io.IOException;
import java.util.List;

public class BlockMetaSaveOnUnloadListener implements Listener
{
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent e)
    {
        try
        {
            Chunk chunk = e.getChunk();
            BlockMetaChunkData chunkMetaData = BlockMetaRepository.getInstance().getChunk(chunk);
            chunkMetaData.save();
        }
        catch (IOException ioEx)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to save chunk meta: %s", DecorHeadsPlugin.PLUGIN_NAME, ioEx.getMessage()));
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPluginDisable(PluginDisableEvent e)
    {
        try
        {
            if (e.getPlugin().getName().equals(DecorHeadsPlugin.getInstance().getName()))
            {
                BlockMetaRepository repo = BlockMetaRepository.getInstance();
                List<World> worlds = Bukkit.getWorlds();
                for (World world : worlds)
                {
                    Chunk[] chunks = world.getLoadedChunks();
                    for (Chunk chunk : chunks)
                    {
                        BlockMetaChunkData chunkMetaData = repo.getChunk(chunk);
                        chunkMetaData.save();
                    }
                }
            }
        }
        catch (IOException ioEx)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to save chunk meta: %s", DecorHeadsPlugin.PLUGIN_NAME, ioEx.getMessage()));
        }
    }
}

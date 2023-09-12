package com.zazsona.decorheads.blockmeta.library.io;

import com.zazsona.decorheads.blockmeta.library.node.ChunkBlockPluginPropertiesNode;
import com.zazsona.decorheads.blockmeta.library.node.IBlockPluginPropertiesNodeParent;
import org.bukkit.World;

import java.io.IOException;

public interface IChunkBlockPluginPropertiesLoader
{
    ChunkBlockPluginPropertiesNode loadChunk(World world, int chunkX, int chunkZ) throws IOException;
}

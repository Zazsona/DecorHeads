package com.zazsona.decorheads.blockmeta.library.io;

import com.zazsona.decorheads.blockmeta.library.node.BlockPluginPropertiesNode;
import org.bukkit.World;

import java.io.IOException;

public interface IBlockPluginPropertiesLoader
{
    BlockPluginPropertiesNode loadBlock(World world, int blockX, int blockY, int blockZ) throws IOException;
}

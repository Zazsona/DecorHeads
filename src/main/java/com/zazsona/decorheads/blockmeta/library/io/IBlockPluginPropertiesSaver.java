package com.zazsona.decorheads.blockmeta.library.io;

import com.zazsona.decorheads.blockmeta.library.node.BlockPluginPropertiesNode;
import com.zazsona.decorheads.blockmeta.library.node.IBlockPluginPropertiesNode;
import org.bukkit.World;

import java.io.IOException;

public interface IBlockPluginPropertiesSaver
{
    void saveBlock(BlockPluginPropertiesNode blockPluginProperties, World world, int blockX, int blockY, int blockZ) throws IOException;
}

package com.zazsona.decorheads.blockmeta.library.io;

import com.zazsona.decorheads.blockmeta.library.node.IChunkBlockPluginPropertiesNodeParent;
import com.zazsona.decorheads.blockmeta.library.node.IRegionBlockPluginPropertiesNodeParent;
import com.zazsona.decorheads.blockmeta.library.node.WorldBlockPluginPropertiesNode;
import org.bukkit.World;

import java.io.IOException;

public interface IWorldBlockPluginPropertiesLoader
{
    WorldBlockPluginPropertiesNode loadWorld(World world) throws IOException;
}

package com.zazsona.decorheads.blockmeta.library.io;

import com.zazsona.decorheads.blockmeta.library.node.IBlockPluginPropertiesNodeParent;
import com.zazsona.decorheads.blockmeta.library.node.IChunkBlockPluginPropertiesNodeParent;
import com.zazsona.decorheads.blockmeta.library.node.RegionBlockPluginPropertiesNode;
import org.bukkit.World;

import java.io.IOException;

public interface IRegionBlockPluginPropertiesLoader
{
    RegionBlockPluginPropertiesNode loadRegion(World world, int regionX, int regionZ) throws IOException;
}

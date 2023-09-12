package com.zazsona.decorheads.blockmeta.library.io;

import com.zazsona.decorheads.blockmeta.library.node.IChunkBlockPluginPropertiesNodeParent;
import com.zazsona.decorheads.blockmeta.library.node.RegionBlockPluginPropertiesNode;
import org.bukkit.World;

import java.io.IOException;

public interface IRegionBlockPluginPropertiesSaver
{
    void saveRegion(RegionBlockPluginPropertiesNode blockPluginProperties, World world, int regionX, int regionZ) throws IOException;
}

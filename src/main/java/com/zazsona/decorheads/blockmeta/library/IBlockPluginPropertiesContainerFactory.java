package com.zazsona.decorheads.blockmeta.library;

import com.zazsona.decorheads.blockmeta.library.container.*;
import org.bukkit.Location;

public interface IBlockPluginPropertiesContainerFactory
{
    IBlockPluginPropertiesContainer makeContainer(Location location, Class<? extends IBlockPluginPropertiesContainer> type);
    IBlockPluginPropertiesContainer makeChunkContainer(Location location);
    IChunkBlockPluginPropertiesContainer makeRegionContainer(Location location);
    IRegionBlockPluginPropertiesContainer makeWorldContainer(Location location);
    IWorldBlockPluginPropertiesContainer makeServerContainer(Location location);
}

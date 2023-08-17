package com.zazsona.decorheads.blockmeta.library;

import com.zazsona.decorheads.blockmeta.library.container.*;
import org.bukkit.Location;

public interface IMutableBlockPluginPropertiesContainerFactory
{
    IMutableBlockPluginPropertiesContainer makeContainer(Location location, Class<? extends IBlockPluginPropertiesContainer> type);
    IMutableBlockPluginPropertiesContainer makeChunkContainer(Location location);
    IMutableChunkBlockPluginPropertiesContainer makeRegionContainer(Location location);
    IMutableRegionBlockPluginPropertiesContainer makeWorldContainer(Location location);
    IMutableWorldBlockPluginPropertiesContainer makeServerContainer(Location location);
}

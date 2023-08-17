package com.zazsona.decorheads.blockmeta.library;

import com.zazsona.decorheads.blockmeta.library.container.*;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Location;

public class BlockPluginPropertiesContainerFactory implements IBlockPluginPropertiesContainerFactory, IMutableBlockPluginPropertiesContainerFactory
{
    @Override
    public IMutableBlockPluginPropertiesContainer makeContainer(Location location, Class<? extends IBlockPluginPropertiesContainer> type)
    {
        if (type == ChunkBlockPluginProperties.class)
            return makeChunkContainer(location);
        if (type == RegionBlockPluginPropertiesContainer.class)
            return makeRegionContainer(location);
        if (type == WorldBlockPluginProperties.class)
            return makeWorldContainer(location);
        if (type == ServerBlockPluginProperties.class)
            return makeServerContainer(location);

        throw new NotImplementedException("Unknown BlockPluginPropertiesContainer: " + type);
    }

    @Override
    public ChunkBlockPluginProperties makeChunkContainer(Location location)
    {
        return new ChunkBlockPluginProperties();
    }

    @Override
    public RegionBlockPluginPropertiesContainer makeRegionContainer(Location location)
    {
        return new RegionBlockPluginPropertiesContainer(this);
    }

    @Override
    public WorldBlockPluginProperties makeWorldContainer(Location location)
    {
        return new WorldBlockPluginProperties(this);
    }

    @Override
    public ServerBlockPluginProperties makeServerContainer(Location location)
    {
        return new ServerBlockPluginProperties(this);
    }
}

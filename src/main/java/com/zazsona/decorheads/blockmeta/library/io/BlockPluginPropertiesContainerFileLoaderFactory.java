package com.zazsona.decorheads.blockmeta.library.io;

import com.zazsona.decorheads.blockmeta.library.BlockPluginPropertiesContainerFactory;
import com.zazsona.decorheads.blockmeta.library.IBlockPluginPropertiesContainerFactory;
import com.zazsona.decorheads.blockmeta.library.IMutableBlockPluginPropertiesContainerFactory;
import com.zazsona.decorheads.blockmeta.library.container.*;
import org.bukkit.Location;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class BlockPluginPropertiesContainerFileLoaderFactory extends BlockPluginPropertiesContainerFactory
{
    private IBlockPluginPropertiesFileStrategy fileStrategy;
    private IBlockPluginPropertiesContainerFileModel fileModel;
    private BlockPluginPropertiesContainerFactory newContainerFactory;

    public BlockPluginPropertiesContainerFileLoaderFactory()
    {
        this.fileStrategy = new JsonBlockPluginPropertiesFileStrategy();
        this.fileModel = new BlockPluginPropertiesContainerFileModel(".json");
        this.newContainerFactory = new BlockPluginPropertiesContainerFactory();
    }

    public BlockPluginPropertiesContainerFileLoaderFactory(IBlockPluginPropertiesFileStrategy fileStrategy, IBlockPluginPropertiesContainerFileModel fileModel)
    {
        this.fileStrategy = fileStrategy;
        this.fileModel = fileModel;
        this.newContainerFactory = new BlockPluginPropertiesContainerFactory();
    }

    public BlockPluginPropertiesContainerFileLoaderFactory(IBlockPluginPropertiesFileStrategy fileStrategy, IBlockPluginPropertiesContainerFileModel fileModel, BlockPluginPropertiesContainerFactory newContainerFactory)
    {
        this.fileStrategy = fileStrategy;
        this.fileModel = fileModel;
        this.newContainerFactory = newContainerFactory;
    }

    @Override
    public ChunkBlockPluginProperties makeChunkContainer(Location location)
    {
        try
        {
            File file = new File(fileModel.getChunkFile(location));
            ChunkBlockPluginProperties properties = fileStrategy.load(file, ChunkBlockPluginProperties.class);
            return properties;
        }
        catch (FileNotFoundException e)
        {
            return newContainerFactory.makeChunkContainer(location);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public RegionBlockPluginPropertiesContainer makeRegionContainer(Location location)
    {
        try
        {
            File file = new File(fileModel.getRegionFile(location));
            RegionBlockPluginPropertiesContainer properties = fileStrategy.load(file, RegionBlockPluginPropertiesContainer.class);
            return properties;
        }
        catch (FileNotFoundException e)
        {
            return newContainerFactory.makeRegionContainer(location);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public WorldBlockPluginProperties makeWorldContainer(Location location)
    {
        try
        {
            File file = new File(fileModel.getWorldFile(location));
            WorldBlockPluginProperties properties = fileStrategy.load(file, WorldBlockPluginProperties.class);
            return properties;
        }
        catch (FileNotFoundException e)
        {
            return newContainerFactory.makeWorldContainer(location);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ServerBlockPluginProperties makeServerContainer(Location location)
    {
        try
        {
            File file = new File(fileModel.getServerFile());
            ServerBlockPluginProperties properties = fileStrategy.load(file, ServerBlockPluginProperties.class);
            return properties;
        }
        catch (FileNotFoundException e)
        {
            return newContainerFactory.makeServerContainer(location);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public IBlockPluginPropertiesFileStrategy getFileStrategy()
    {
        return fileStrategy;
    }

    public void setFileStrategy(IBlockPluginPropertiesFileStrategy fileStrategy)
    {
        this.fileStrategy = fileStrategy;
    }

    public IBlockPluginPropertiesContainerFileModel getFileModel()
    {
        return fileModel;
    }

    public void setFileModel(IBlockPluginPropertiesContainerFileModel fileModel)
    {
        this.fileModel = fileModel;
    }

    public BlockPluginPropertiesContainerFactory getNewContainerFactory()
    {
        return newContainerFactory;
    }

    public void setNewContainerFactory(BlockPluginPropertiesContainerFactory newContainerFactory)
    {
        this.newContainerFactory = newContainerFactory;
    }
}

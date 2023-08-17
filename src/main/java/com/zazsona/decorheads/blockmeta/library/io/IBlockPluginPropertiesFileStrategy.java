package com.zazsona.decorheads.blockmeta.library.io;

import com.zazsona.decorheads.blockmeta.library.container.IBlockPluginPropertiesContainer;

import java.io.File;
import java.io.IOException;

public interface IBlockPluginPropertiesFileStrategy
{
    <T extends IBlockPluginPropertiesContainer> T load(File file, Class<T> typeOfContainer) throws IOException;

    File save(File file, IBlockPluginPropertiesContainer blockPluginPropertiesContainer) throws IOException;
}

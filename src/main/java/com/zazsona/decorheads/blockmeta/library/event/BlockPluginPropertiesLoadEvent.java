package com.zazsona.decorheads.blockmeta.library.event;

import com.zazsona.decorheads.blockmeta.library.container.IBlockPluginPropertiesContainer;

public class BlockPluginPropertiesLoadEvent
{
    private IBlockPluginPropertiesContainer propertiesContainer;

    public BlockPluginPropertiesLoadEvent(IBlockPluginPropertiesContainer propertiesContainer)
    {
        this.propertiesContainer = propertiesContainer;
    }

    /**
     * Gets propertiesContainer
     * @return propertiesContainer
     */
    public IBlockPluginPropertiesContainer getPropertiesContainer()
    {
        return propertiesContainer;
    }
}

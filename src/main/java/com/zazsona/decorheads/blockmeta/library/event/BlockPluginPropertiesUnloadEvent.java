package com.zazsona.decorheads.blockmeta.library.event;

import com.zazsona.decorheads.blockmeta.library.container.IBlockPluginPropertiesContainer;

public class BlockPluginPropertiesUnloadEvent extends BlockPluginPropertyEvent
{
    private IBlockPluginPropertiesContainer propertiesContainer;

    public BlockPluginPropertiesUnloadEvent(IBlockPluginPropertiesContainer propertiesContainer)
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

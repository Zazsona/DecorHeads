package com.zazsona.decorheads.blockmeta.library.event;

import com.zazsona.decorheads.blockmeta.library.node.IBlockPluginPropertiesNode;

public class BlockPluginPropertiesLoadEvent
{
    private IBlockPluginPropertiesNode propertiesContainer;

    public BlockPluginPropertiesLoadEvent(IBlockPluginPropertiesNode propertiesContainer)
    {
        this.propertiesContainer = propertiesContainer;
    }

    /**
     * Gets propertiesContainer
     * @return propertiesContainer
     */
    public IBlockPluginPropertiesNode getPropertiesContainer()
    {
        return propertiesContainer;
    }
}

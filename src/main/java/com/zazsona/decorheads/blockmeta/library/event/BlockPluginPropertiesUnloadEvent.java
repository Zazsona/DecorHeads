package com.zazsona.decorheads.blockmeta.library.event;

import com.zazsona.decorheads.blockmeta.library.node.IBlockPluginPropertiesNode;

public class BlockPluginPropertiesUnloadEvent
{
    private IBlockPluginPropertiesNode propertiesContainer;

    public BlockPluginPropertiesUnloadEvent(IBlockPluginPropertiesNode propertiesContainer)
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

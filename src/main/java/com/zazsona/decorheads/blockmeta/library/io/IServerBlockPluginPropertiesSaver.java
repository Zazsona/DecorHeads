package com.zazsona.decorheads.blockmeta.library.io;

import com.zazsona.decorheads.blockmeta.library.node.IRegionBlockPluginPropertiesNodeParent;
import com.zazsona.decorheads.blockmeta.library.node.IWorldBlockPluginPropertiesNodeParent;
import com.zazsona.decorheads.blockmeta.library.node.ServerBlockPluginPropertiesNode;
import org.bukkit.World;

import java.io.IOException;

public interface IServerBlockPluginPropertiesSaver
{
    void saveServer(ServerBlockPluginPropertiesNode blockPluginProperties) throws IOException;
}

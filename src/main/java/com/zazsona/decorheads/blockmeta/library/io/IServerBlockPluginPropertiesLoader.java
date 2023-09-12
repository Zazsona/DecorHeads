package com.zazsona.decorheads.blockmeta.library.io;

import com.zazsona.decorheads.blockmeta.library.node.IWorldBlockPluginPropertiesNodeParent;
import com.zazsona.decorheads.blockmeta.library.node.ServerBlockPluginPropertiesNode;

import java.io.IOException;

public interface IServerBlockPluginPropertiesLoader
{
    ServerBlockPluginPropertiesNode loadServer() throws IOException;
}

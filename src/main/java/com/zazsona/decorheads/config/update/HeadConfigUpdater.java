package com.zazsona.decorheads.config.update;

import com.zazsona.decorheads.config.HeadConfig;
import com.zazsona.decorheads.config.VersionedYamlConfigWrapper;

import java.io.IOException;

public class HeadConfigUpdater extends VersionedYamlConfigUpdater implements IVersionedYamlConfigUpdater<HeadConfig>
{
    public HeadConfigUpdater()
    {
        super("updates/heads", "base.yml");
    }

    public HeadConfig update(HeadConfig config) throws IOException
    {
        return (HeadConfig) super.updateToVersion(config, VersionedYamlConfigWrapper.MAX_CONFIG_VERSION);
    }

    public HeadConfig update(HeadConfig config, String targetVersion) throws IOException
    {
        return (HeadConfig) super.updateToVersion(config, targetVersion);
    }
}

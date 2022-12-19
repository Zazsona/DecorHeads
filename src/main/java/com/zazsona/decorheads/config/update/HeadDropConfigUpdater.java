package com.zazsona.decorheads.config.update;

import com.zazsona.decorheads.config.HeadConfig;
import com.zazsona.decorheads.config.HeadDropConfig;
import com.zazsona.decorheads.config.VersionedYamlConfigWrapper;

import java.io.IOException;

public class HeadDropConfigUpdater extends VersionedYamlConfigUpdater implements IVersionedYamlConfigUpdater<HeadDropConfig>
{
    public HeadDropConfigUpdater()
    {
        super("updates/drops", "base.yml");
    }

    public HeadDropConfig update(HeadDropConfig config) throws IOException, InstantiationException
    {
        return (HeadDropConfig) super.updateToVersion(config, VersionedYamlConfigWrapper.MAX_CONFIG_VERSION);
    }

    public HeadDropConfig update(HeadDropConfig config, String targetVersion) throws IOException, InstantiationException
    {
        return (HeadDropConfig) super.updateToVersion(config, targetVersion);
    }
}

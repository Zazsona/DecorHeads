package com.zazsona.decorheads.config.update;

import com.zazsona.decorheads.config.HeadConfig;
import com.zazsona.decorheads.config.LegacyHeadConfig;
import com.zazsona.decorheads.config.VersionedYamlConfigWrapper;

import java.io.IOException;

public class LegacyHeadConfigUpdater extends VersionedYamlConfigUpdater implements IVersionedYamlConfigUpdater<LegacyHeadConfig>
{
    public LegacyHeadConfigUpdater()
    {
        super("updates/legacy", "base.yml");
    }

    public LegacyHeadConfig update(LegacyHeadConfig config) throws IOException, InstantiationException
    {
        return (LegacyHeadConfig) super.updateToVersion(config, "2.3.0");
    }

    public LegacyHeadConfig update(LegacyHeadConfig config, String targetVersion) throws IOException, InstantiationException
    {
        return (LegacyHeadConfig) super.updateToVersion(config, targetVersion);
    }
}

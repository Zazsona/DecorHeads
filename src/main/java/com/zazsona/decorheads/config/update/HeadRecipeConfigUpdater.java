package com.zazsona.decorheads.config.update;

import com.zazsona.decorheads.config.HeadConfig;
import com.zazsona.decorheads.config.HeadRecipeConfig;
import com.zazsona.decorheads.config.VersionedYamlConfigWrapper;

import java.io.IOException;

public class HeadRecipeConfigUpdater extends VersionedYamlConfigUpdater implements IVersionedYamlConfigUpdater<HeadRecipeConfig>
{
    public HeadRecipeConfigUpdater()
    {
        super("updates/recipes", "base.yml");
    }

    public HeadRecipeConfig update(HeadRecipeConfig config) throws IOException, InstantiationException
    {
        return (HeadRecipeConfig) super.updateToVersion(config, HeadRecipeConfig.getMaxConfigVersion());
    }

    public HeadRecipeConfig update(HeadRecipeConfig config, String targetVersion) throws IOException, InstantiationException
    {
        return (HeadRecipeConfig) super.updateToVersion(config, targetVersion);
    }
}

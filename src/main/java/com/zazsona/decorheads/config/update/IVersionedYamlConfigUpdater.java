package com.zazsona.decorheads.config.update;

import com.zazsona.decorheads.config.VersionedYamlConfigWrapper;

import java.io.IOException;

public interface IVersionedYamlConfigUpdater<T extends VersionedYamlConfigWrapper>
{
    /**
     * Updates the provided config to the latest available version
     * @param config the config to update
     * @return the updated config
     * @throws IOException error accessing update data
     */
    T update(T config) throws IOException, InstantiationException;

    /**
     * Updates the provided config to the target version
     * @param config the config to update
     * @param targetVersion the version to update to
     * @return the updated config
     * @throws IOException error accessing update data
     */
    T update(T config, String targetVersion) throws IOException, InstantiationException;
}

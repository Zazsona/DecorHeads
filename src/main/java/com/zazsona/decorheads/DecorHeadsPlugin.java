package com.zazsona.decorheads;

import com.zazsona.decorheads.blockmeta.BlockInventoryOwnerBlockPropertiesUpdater;
import com.zazsona.decorheads.blockmeta.HeadBlockPropertiesUpdater;
import com.zazsona.decorheads.blockmeta.library.node.LoadedBlockPluginProperties;
import com.zazsona.decorheads.command.ConfigCommand;
import com.zazsona.decorheads.command.MasterCommand;
import com.zazsona.decorheads.command.SpawnHeadCommand;
import com.zazsona.decorheads.command.WikiCommand;
import com.zazsona.decorheads.config.*;
import com.zazsona.decorheads.config.update.*;
import com.zazsona.decorheads.crafting.MetaRecipeManager;
import com.zazsona.decorheads.event.BlockBreakByExplosionEventTrigger;
import com.zazsona.decorheads.event.BlockInventoryOwnerChangeEventTrigger;
import com.zazsona.decorheads.event.BlockPistonReactionEventTrigger;
import com.zazsona.decorheads.event.RegionEventTrigger;
import com.zazsona.decorheads.event.head.HeadEventTrigger;
import org.bukkit.plugin.java.JavaPlugin;

import javax.naming.ConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

public class DecorHeadsPlugin extends JavaPlugin
{
    public static final String PLUGIN_NAME = "DecorHeads";

    public static DecorHeadsPlugin getInstance()
    {
        return DecorHeadsPlugin.getPlugin(DecorHeadsPlugin.class);
    }

    public static PluginConfig getInstanceConfig()
    {
        return DecorHeadsPlugin.getInstance().getPluginConfig();
    }

    private PluginConfig pluginConfig;
    private HeadConfig headConfig;
    private HeadDropConfig headDropConfig;
    private HeadRecipeConfig headRecipeConfig;

    /**
     * Returns the default config available via {@link #getConfig()} wrapped with {@link PluginConfig}
     * @return pluginConfig the wrapped config
     */
    public PluginConfig getPluginConfig()
    {
        return pluginConfig;
    }
    public HeadConfig getHeadConfig()
    {
        return headConfig;
    }
    public HeadDropConfig getHeadDropConfig()
    {
        return headDropConfig;
    }
    public HeadRecipeConfig getHeadRecipeConfig()
    {
        return headRecipeConfig;
    }

    @Override
    public void onEnable()
    {
        try
        {
            // ============================================
            // Initialisers
            // ============================================
            MetaRecipeManager.getInstance(this); // Call constructor
            File pluginConfigFile = Paths.get(getDataFolder().toString(), "config.yml").toFile();
            File headConfigFile = Paths.get(getDataFolder().getAbsolutePath(), "heads.yml").toFile();
            File dropsConfigFile = Paths.get(getDataFolder().toString(), "drops.yml").toFile();
            File recipesConfigFile = Paths.get(getDataFolder().toString(), "recipes.yml").toFile();

            if (LegacyHeadConfig.isLegacyConfig(headConfigFile))
            {
                LegacyHeadConfig legacyHeadConfig = new LegacyHeadConfig(headConfigFile);
                LegacyHeadConfigUpdater updater = new LegacyHeadConfigUpdater();
                legacyHeadConfig = updater.update(legacyHeadConfig);
                LegacyHeadConfigMigrator migrator = new LegacyHeadConfigMigrator();
                Map<Class<? extends VersionedYamlConfigWrapper>, VersionedYamlConfigWrapper> migratedConfigs = migrator.migrate(legacyHeadConfig, headConfigFile, dropsConfigFile, recipesConfigFile);
                migratedConfigs.forEach((aClass, configWrapper) -> { try { configWrapper.save(); } catch (IOException e) { e.printStackTrace(); } });
            }

            pluginConfig = new PluginConfig(getConfig(), pluginConfigFile); // Call getConfig() here to match reference stored by Bukkit internally
            headConfig = new HeadConfig(headConfigFile);
            headDropConfig = new HeadDropConfig(dropsConfigFile);
            headRecipeConfig = new HeadRecipeConfig(recipesConfigFile);

            // ============================================
            // Updaters
            // ============================================
            PluginConfigUpdater pluginConfigUpdater = new PluginConfigUpdater();
            pluginConfigUpdater.update(pluginConfig, PluginConfig.getMaxConfigVersion());
            if (pluginConfig.validateConfigData())
                pluginConfig.save();
            else
                throw new ConfigurationException("Plugin Config is invalid.");

            HeadConfigUpdater headConfigUpdater = new HeadConfigUpdater();
            headConfigUpdater.update(headConfig, HeadConfig.getMaxConfigVersion());
            if (headConfig.validateConfigData())
                headConfig.save();
            else
                throw new ConfigurationException("Heads Config is invalid.");

            HeadDropConfigUpdater headDropConfigUpdater = new HeadDropConfigUpdater();
            headDropConfigUpdater.update(headDropConfig, HeadDropConfig.getMaxConfigVersion());
            if (headDropConfig.validateConfigData())
                headDropConfig.save();
            else
                throw new ConfigurationException("Drops Config is invalid.");

            HeadRecipeConfigUpdater headRecipeConfigUpdater = new HeadRecipeConfigUpdater();
            headRecipeConfigUpdater.update(headRecipeConfig, HeadRecipeConfig.getMaxConfigVersion());
            if (headRecipeConfig.validateConfigData())
                headRecipeConfig.save();
            else
                throw new ConfigurationException("Recipes Config is invalid.");

            // ============================================
            // Asset Loaders
            // ============================================
            HeadRepository.loadConfig(this);
            MaterialUtil.indexMaterials();

            // ============================================
            // Permissions
            // ============================================
            getServer().getPluginManager().registerEvents(new HeadCraftBlocker(), this);

            // ============================================
            // Commands
            // ============================================
            MasterCommand masterCommand = new MasterCommand();
            SpawnHeadCommand spawnHeadCommand = new SpawnHeadCommand();
            spawnHeadCommand.loadCommandHeads();
            WikiCommand wikiCommand = new WikiCommand();
            ConfigCommand configCommand = new ConfigCommand();

            getCommand(PLUGIN_NAME).setExecutor(masterCommand);
            getCommand(SpawnHeadCommand.COMMAND_KEY).setExecutor(spawnHeadCommand);
            getCommand(WikiCommand.COMMAND_KEY).setExecutor(wikiCommand);
            getCommand(ConfigCommand.COMMAND_KEY).setExecutor(configCommand);

            // ============================================
            // Plugin Stat Events
            // ============================================
            MetricsManager.getInstance().enable();
            UpdateNotifier updateNotifier = new UpdateNotifier();
            getServer().getPluginManager().registerEvents(updateNotifier, this);

            // ============================================
            // Bukkit Events
            // ============================================
            BlockInventoryOwnerChangeEventTrigger blockInventoryOwnerChangeEventTrigger = new BlockInventoryOwnerChangeEventTrigger();
            getServer().getPluginManager().registerEvents(blockInventoryOwnerChangeEventTrigger, this);

            BlockBreakByExplosionEventTrigger blockBreakByExplosionEventTrigger = new BlockBreakByExplosionEventTrigger();
            getServer().getPluginManager().registerEvents(blockBreakByExplosionEventTrigger, this);

            BlockPistonReactionEventTrigger blockPistonReactionEventTrigger = new BlockPistonReactionEventTrigger();
            getServer().getPluginManager().registerEvents(blockPistonReactionEventTrigger, this);

            HeadEventTrigger headEventTrigger = new HeadEventTrigger();
            getServer().getPluginManager().registerEvents(headEventTrigger, this);

            RegionEventTrigger regionEventTrigger = new RegionEventTrigger();
            getServer().getPluginManager().registerEvents(regionEventTrigger, this);

            // ============================================
            // Block Metadata
            // ============================================
            LoadedBlockPluginProperties blockPluginProperties = LoadedBlockPluginProperties.getInstance(this);
            getServer().getPluginManager().registerEvents(blockPluginProperties, this);

            BlockInventoryOwnerBlockPropertiesUpdater inventoryOwnerMetaListener = new BlockInventoryOwnerBlockPropertiesUpdater();
            getServer().getPluginManager().registerEvents(inventoryOwnerMetaListener, this);

            HeadBlockPropertiesUpdater headBlockMetaListener = new HeadBlockPropertiesUpdater();
            getServer().getPluginManager().registerEvents(headBlockMetaListener, this);
        }
        catch (Exception e)
        {
            getLogger().warning(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable()
    {
        HeadRepository.unloadHeads();
        HeadRepository.unloadDrops();
        HeadRepository.unloadRecipes(this);
    }

    @Override
    public void reloadConfig()
    {
        super.reloadConfig();
        if (pluginConfig != null)
            pluginConfig.setConfigData(getConfig());
    }
}

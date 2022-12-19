package com.zazsona.decorheads;

import com.zazsona.decorheads.blockmeta.BlockInventoryOwnerListener;
import com.zazsona.decorheads.blockmeta.BlockMetaRepository;
import com.zazsona.decorheads.blockmeta.BlockMetaSaveOnUnloadListener;
import com.zazsona.decorheads.blockmeta.HeadBlockModificationListener;
import com.zazsona.decorheads.command.ConfigCommand;
import com.zazsona.decorheads.command.MasterCommand;
import com.zazsona.decorheads.command.SpawnHeadCommand;
import com.zazsona.decorheads.command.WikiCommand;
import com.zazsona.decorheads.config.*;
import com.zazsona.decorheads.config.update.HeadConfigUpdater;
import com.zazsona.decorheads.config.update.HeadDropConfigUpdater;
import com.zazsona.decorheads.config.update.HeadRecipeConfigUpdater;
import com.zazsona.decorheads.config.update.PluginConfigUpdater;
import com.zazsona.decorheads.event.block.BlockBreakByExplosionEventTrigger;
import com.zazsona.decorheads.event.block.BlockPistonReactionEventTrigger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Paths;

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
            MaterialUtil.indexMaterials();
            pluginConfig = new PluginConfig(getConfig(), Paths.get(getDataFolder().toString(), "config.yml").toFile()); // Call getConfig() here to match reference stored by Bukkit internally
            headConfig = new HeadConfig(Paths.get(getDataFolder().toString(), "heads.yml").toFile());
            headDropConfig = new HeadDropConfig(Paths.get(getDataFolder().toString(), "drops.yml").toFile());
            headRecipeConfig = new HeadRecipeConfig(Paths.get(getDataFolder().toString(), "recipes.yml").toFile());

            // ============================================
            // Updaters
            // ============================================
            PluginConfigUpdater pluginConfigUpdater = new PluginConfigUpdater();
            pluginConfigUpdater.update(pluginConfig, PluginConfig.MAX_CONFIG_VERSION);
            pluginConfig.save();

            HeadConfigUpdater headConfigUpdater = new HeadConfigUpdater();
            headConfigUpdater.update(headConfig, HeadConfig.MAX_CONFIG_VERSION);
            headConfig.save();

            HeadDropConfigUpdater headDropConfigUpdater = new HeadDropConfigUpdater();
            headDropConfigUpdater.update(headDropConfig, HeadDropConfig.MAX_CONFIG_VERSION);
            headDropConfig.save();

            HeadRecipeConfigUpdater headRecipeConfigUpdater = new HeadRecipeConfigUpdater();
            headRecipeConfigUpdater.update(headRecipeConfig, HeadRecipeConfig.MAX_CONFIG_VERSION);
            headRecipeConfig.save();

            HeadUpdaterLegacy headUpdater = HeadUpdaterLegacy.getInstance();
            headUpdater.updateHeadsFile();

            HeadRepository.loadConfig(this);

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
            BlockBreakByExplosionEventTrigger blockBreakByExplosionEventTrigger = new BlockBreakByExplosionEventTrigger();
            getServer().getPluginManager().registerEvents(blockBreakByExplosionEventTrigger, this);

            BlockPistonReactionEventTrigger blockPistonReactionEventTrigger = new BlockPistonReactionEventTrigger();
            getServer().getPluginManager().registerEvents(blockPistonReactionEventTrigger, this);

            // ============================================
            // Block Metadata
            // ============================================
            BlockMetaRepository blockMetaRepository = BlockMetaRepository.getInstance();
            getServer().getPluginManager().registerEvents(blockMetaRepository, this);

            BlockMetaSaveOnUnloadListener saveMetaListener = new BlockMetaSaveOnUnloadListener();
            getServer().getPluginManager().registerEvents(saveMetaListener, this);

            BlockInventoryOwnerListener inventoryOwnerMetaListener = new BlockInventoryOwnerListener();
            getServer().getPluginManager().registerEvents(inventoryOwnerMetaListener, this);

            HeadBlockModificationListener headBlockMetaListener = new HeadBlockModificationListener();
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
        HeadRepository.unloadRecipes();
    }

    @Override
    public void reloadConfig()
    {
        super.reloadConfig();
        pluginConfig.setConfigData(getConfig());
    }
}

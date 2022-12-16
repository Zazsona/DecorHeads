package com.zazsona.decorheads;

import com.zazsona.decorheads.blockmeta.BlockInventoryOwnerListener;
import com.zazsona.decorheads.blockmeta.BlockMetaRepository;
import com.zazsona.decorheads.blockmeta.BlockMetaSaveOnUnloadListener;
import com.zazsona.decorheads.blockmeta.HeadBlockModificationListener;
import com.zazsona.decorheads.command.ConfigCommand;
import com.zazsona.decorheads.command.MasterCommand;
import com.zazsona.decorheads.command.SpawnHeadCommand;
import com.zazsona.decorheads.command.WikiCommand;
import com.zazsona.decorheads.config.ConfigUpdater;
import com.zazsona.decorheads.config.HeadConfig;
import com.zazsona.decorheads.config.HeadUpdater;
import com.zazsona.decorheads.event.block.BlockBreakByExplosionEventTrigger;
import com.zazsona.decorheads.event.block.BlockPistonReactionEventTrigger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin
{
    public static final String PLUGIN_NAME = "DecorHeads";

    public static JavaPlugin getSelfPlugin()
    {
        return Core.getPlugin(Core.class);
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

            // ============================================
            // Updaters
            // ============================================
            ConfigUpdater configUpdater = ConfigUpdater.getInstance();
            configUpdater.updateConfig();

            HeadUpdater headUpdater = HeadUpdater.getInstance();
            headUpdater.updateHeadsFile();

            HeadConfig.loadConfig(this);

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
            Bukkit.getLogger().warning(String.format("[%s] %s", Core.PLUGIN_NAME, e.getMessage()));
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable()
    {
        HeadConfig.unloadHeads();
        HeadConfig.unloadDrops();
        HeadConfig.unloadRecipes();
    }
}

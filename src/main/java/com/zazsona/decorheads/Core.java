package com.zazsona.decorheads;

import com.zazsona.decorheads.blockmeta.BlockInventoryOwnerListener;
import com.zazsona.decorheads.blockmeta.BlockMetaLogger;
import com.zazsona.decorheads.blockmeta.HeadBlockListener;
import com.zazsona.decorheads.command.ConfigCommand;
import com.zazsona.decorheads.command.MasterCommand;
import com.zazsona.decorheads.command.SpawnHeadCommand;
import com.zazsona.decorheads.command.WikiCommand;
import com.zazsona.decorheads.config.ConfigUpdater;
import com.zazsona.decorheads.config.HeadLoader;
import com.zazsona.decorheads.config.HeadUpdater;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

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
            ConfigUpdater configUpdater = ConfigUpdater.getInstance();
            configUpdater.updateConfig();
            HeadUpdater headUpdater = HeadUpdater.getInstance();
            headUpdater.updateHeadsFile();
            HeadLoader headLoader = HeadLoader.getInstance();
            headLoader.loadHeads();

            MaterialUtil.indexMaterials();
            getServer().getPluginManager().registerEvents(new HeadBlockListener(), this);
            getServer().getPluginManager().registerEvents(new HeadCraftBlocker(), this);
            getServer().getPluginManager().registerEvents(BlockInventoryOwnerListener.getInstance(), this);
            BlockMetaLogger.getInstance().loadFromFile();
            scheduleBlockDataSaving();

            getCommand(PLUGIN_NAME).setExecutor(new MasterCommand());
            getCommand(SpawnHeadCommand.COMMAND_KEY).setExecutor(new SpawnHeadCommand());
            getCommand(WikiCommand.COMMAND_KEY).setExecutor(new WikiCommand());
            getCommand(ConfigCommand.COMMAND_KEY).setExecutor(new ConfigCommand());

            MetricsManager.getInstance().enable();

            UpdateNotifier updateNotifier = new UpdateNotifier();
            getServer().getPluginManager().registerEvents(updateNotifier, this);
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
        try
        {
            BlockMetaLogger.getInstance().saveToFile();
        }
        catch (IOException e)
        {
            Bukkit.getLogger().severe(String.format("[%s] Unable to save block data: %s", PLUGIN_NAME, e.getMessage()));
        }
        finally
        {
            super.onDisable();
        }
    }

    private void scheduleBlockDataSaving()
    {
        Bukkit.getScheduler().runTaskTimerAsynchronously(getSelfPlugin(), () ->
        {
            try
            {
                BlockMetaLogger blockMetaLogger = BlockMetaLogger.getInstance();
                if (blockMetaLogger.isDirty())
                    blockMetaLogger.saveToFile();
            }
            catch (IOException e)
            {
                Bukkit.getLogger().severe(String.format("[%s] Unable to save block data: %s", PLUGIN_NAME, e.getMessage()));
            }
        }, 600, 600);
    }
}

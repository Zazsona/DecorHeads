import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class Core extends JavaPlugin
{
    private static Settings settings;

    @Override
    public void onEnable()
    {
        try
        {
            settings = new Settings(this.getDataFolder());
            if (settings.Enabled && settings.DropsEnabled)
            {
                getServer().getPluginManager().registerEvents(new HeadDropListener(), this);
            }
            this.getCommand("DecorHeads").setExecutor(new DecorHeadsCommand());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onDisable()
    {
    }

    public static Settings getSettings()
    {
        return settings;
    }
}

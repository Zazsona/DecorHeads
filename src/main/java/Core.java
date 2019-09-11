import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin
{
    @Override
    public void onEnable()
    {
        getConfig().options().copyDefaults(true);
        saveConfig();
        getServer().getPluginManager().registerEvents(new HeadDropListener(), this);
        this.getCommand("DecorHeads").setExecutor(new DecorHeadsCommand());
    }

    @Override
    public void onDisable()
    {
    }
}

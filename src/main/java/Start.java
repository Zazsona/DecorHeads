import org.bukkit.plugin.java.JavaPlugin;

public class Start extends JavaPlugin
{
    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(new HeadDropListener(), this);
        //this.getCommand("HelloWorld").setExecutor(new Command());
    }

    @Override
    public void onDisable()
    {
    }
}

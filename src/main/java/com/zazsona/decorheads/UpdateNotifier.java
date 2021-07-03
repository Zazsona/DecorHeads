package com.zazsona.decorheads;

import com.zazsona.decorheads.config.PluginConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class UpdateNotifier implements Listener
{
    private static final String spigotResourceId = "88633";
    private Plugin plugin;
    private String latestVersion;

    public UpdateNotifier()
    {
        plugin = Core.getSelfPlugin();
        fetchLatestVersion();
    }

    private void fetchLatestVersion()
    {
        Bukkit.getScheduler().runTaskAsynchronously(Core.getSelfPlugin(), () -> latestVersion = getLatestVersion());
        latestVersion = getLatestVersion();
    }

    private String getLatestVersion()
    {
        try
        {
            URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource="+spigotResourceId);
            BufferedReader apiReader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = apiReader.readLine()) != null)
            {
                responseBuilder.append(line).append("\n");
            }
            return responseBuilder.toString().trim();
        }
        catch (IOException e)
        {
            Bukkit.getLogger().warning(String.format("[%s] Unable to fetch latest version.", Core.PLUGIN_NAME));
            return null;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        if (latestVersion != null && e.getPlayer().isOp() && PluginConfig.isUpdateNotificationsEnabled())
        {
            String currentVersion = plugin.getDescription().getVersion();
            if (!currentVersion.equalsIgnoreCase(latestVersion))
            {
                e.getPlayer().sendMessage(String.format(ChatColor.GREEN+"A new version of %s is available! (%s -> %s)", Core.PLUGIN_NAME, currentVersion, latestVersion));
            }
        }
    }
}

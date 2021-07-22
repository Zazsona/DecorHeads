package com.zazsona.decorheads;

import com.zazsona.decorheads.config.PluginConfig;
import com.zazsona.decorheads.config.UpdateNotificationLevel;
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
    private String currentVersion;
    private String latestVersion;
    private UpdateNotificationLevel updateLevel;

    public UpdateNotifier()
    {
        this.plugin = Core.getSelfPlugin();
        this.currentVersion = plugin.getDescription().getVersion();
        this.latestVersion = currentVersion;            // Fetch query is async, so these are default values until that returns a result.
        this.updateLevel = UpdateNotificationLevel.DISABLED;

        Bukkit.getScheduler().runTaskAsynchronously(Core.getSelfPlugin(), () ->
        {
            latestVersion = getLatestVersion();
            updateLevel = getUpdateLevel(currentVersion, latestVersion);
        });
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

    private UpdateNotificationLevel getUpdateLevel(String currentVersion, String latestVersion)
    {
        if (latestVersion.compareTo(currentVersion) > 0)
        {
            String[] currentVerDigits = currentVersion.split("[.]");
            String[] latestVerDigits = latestVersion.split("[.]");
            int digitCount = 3; //X.Y.Z
            for (int i = 0; i < digitCount; i++)
            {
                String currentVerDigit = (i < currentVerDigits.length) ? currentVerDigits[i] : "0";
                String latestVerDigit = (i < latestVerDigits.length) ? latestVerDigits[i] : "0";
                if (latestVerDigit.compareTo(currentVerDigit) > 0)
                {
                    switch (i)
                    {
                        case 0:
                            return UpdateNotificationLevel.MAJOR;
                        case 1:
                            return UpdateNotificationLevel.MINOR;
                        case 2:
                            return UpdateNotificationLevel.PATCH;
                    }
                }
            }
        }
        return UpdateNotificationLevel.DISABLED;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        UpdateNotificationLevel notifLevel = PluginConfig.getUpdateNotificationsLevel();
        if (e.getPlayer().isOp())
        {
            if ((updateLevel == UpdateNotificationLevel.MAJOR && (notifLevel == UpdateNotificationLevel.MAJOR || notifLevel == UpdateNotificationLevel.MINOR || notifLevel == UpdateNotificationLevel.PATCH))
                    || (updateLevel == UpdateNotificationLevel.MINOR && (notifLevel == UpdateNotificationLevel.MINOR || notifLevel == UpdateNotificationLevel.PATCH))
                    || (updateLevel == UpdateNotificationLevel.PATCH && notifLevel == UpdateNotificationLevel.PATCH))
            {
                e.getPlayer().sendMessage(String.format(ChatColor.GREEN+"A new version of %s is available! (%s -> %s)", Core.PLUGIN_NAME, currentVersion, latestVersion));
            }
        }
    }
}

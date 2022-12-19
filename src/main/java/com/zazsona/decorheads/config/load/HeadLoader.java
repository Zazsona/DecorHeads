package com.zazsona.decorheads.config.load;

import com.zazsona.decorheads.DecorHeadsPlugin;
import com.zazsona.decorheads.config.HeadConfig;
import com.zazsona.decorheads.HeadType;
import com.zazsona.decorheads.exceptions.MissingFieldsException;
import com.zazsona.decorheads.headdata.IHead;
import com.zazsona.decorheads.headdata.PlayerHead;
import com.zazsona.decorheads.headdata.TextureHead;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Set;

import static com.zazsona.decorheads.config.HeadConfig.*;

public class HeadLoader
{
    /**
     * Loads all the heads in the provided config
     * @param headsConfig the heads config
     * @return a key:head map of registered heads
     * @throws MissingFieldsException invalid config
     */
    public HashMap<String, IHead> loadHeads(HeadConfig headsConfig) throws MissingFieldsException
    {
        ConfigurationSection heads = headsConfig.getHeads();
        Set<String> headKeys = headsConfig.getHeadKeys();

        HashMap<String, IHead> loadedHeads = new HashMap<>();
        for (String headKey : headKeys)
        {
            try
            {
                ConfigurationSection headData = heads.getConfigurationSection(headKey);
                IHead head = loadHead(headData);
                if (loadedHeads.containsKey(head.getKey()))
                    throw new IllegalArgumentException(String.format("Duplicate Head Key: %s", head.getKey()));
                else
                    loadedHeads.put(head.getKey(), head);
            }
            catch (Exception e)
            {
                Bukkit.getLogger().warning(String.format("[%s] %s", DecorHeadsPlugin.PLUGIN_NAME, e.getMessage()));
            }
        }
        return loadedHeads;
    }

    /**
     * Loads a specific head.
     * @param headYaml the head data
     * @return the loaded head
     * @throws IllegalArgumentException head failed to load
     */
    public IHead loadHead(ConfigurationSection headYaml) throws IllegalArgumentException
    {
        try
        {
            IHead head = parseHead(headYaml);
            return head;
        }
        catch (Exception e)
        {
            String name = (headYaml.getName() != null ? headYaml.getString(headYaml.getName()) : "[UNKNOWN]");
            throw new IllegalArgumentException(String.format("Unable to load head \"%s\": %s", name, e.getMessage()));
        }
    }

    /**
     * Parses head YAML data and converts it into an IHead implementation
     * @param headYaml the head to parse
     * @return an {@link IHead}
     * @throws MissingFieldsException invalid head data
     * @throws IllegalArgumentException invalid head data
     */
    private IHead parseHead(ConfigurationSection headYaml) throws MissingFieldsException, IllegalArgumentException
    {
        if (headYaml.getName() == null)
            throw new MissingFieldsException("Missing Key: Head Key");
        if (!headYaml.contains(HEAD_TYPE_KEY))
            throw new MissingFieldsException(String.format("Missing Field: %s", HEAD_TYPE_KEY), HEAD_TYPE_KEY);
        if (!headYaml.contains(HEAD_NAME_KEY))
            throw new MissingFieldsException(String.format("Missing Field: %s", HEAD_NAME_KEY), HEAD_NAME_KEY);

        String key = headYaml.getName().toLowerCase();
        String headTypeName = headYaml.getString(HEAD_TYPE_KEY);
        String name = headYaml.getString(HEAD_NAME_KEY);

        HeadType headType = HeadType.matchHeadType(headTypeName);

        if (headType == HeadType.DECOR)
        {
            if (!headYaml.contains(HEAD_TEXTURE_KEY))
                throw new MissingFieldsException(String.format("Missing Field: %s", HEAD_TEXTURE_KEY), HEAD_TEXTURE_KEY);
            String texture = headYaml.getString(HEAD_TEXTURE_KEY);
            return new TextureHead(key, name, texture);
        }
        else if (headType == HeadType.PLAYER)
            return new PlayerHead(key, name);
        else
            throw new IllegalArgumentException(String.format("Invalid head type: %s", headTypeName));
    }
}

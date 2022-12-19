package com.zazsona.decorheads.config;

import com.zazsona.decorheads.DecorHeadsPlugin;
import com.zazsona.decorheads.exceptions.MissingFieldsException;
import com.zazsona.decorheads.headdata.IHead;
import com.zazsona.decorheads.headdata.PlayerHead;
import com.zazsona.decorheads.headdata.TextureHead;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;

import static com.zazsona.decorheads.config.HeadConfig.*;

class HeadLoader
{
    /**
     * Loads all the heads in the provided data, provided it complies with the expected format.
     * @param headsYaml the head data
     * @return a key:head map of registered heads
     * @throws MissingFieldsException invalid YAML format
     */
    public HashMap<String, IHead> loadHeads(ConfigurationSection headsYaml) throws MissingFieldsException
    {
        if (!headsYaml.contains(HEADS_KEY))
            throw new MissingFieldsException(String.format("Missing Field: %s", HEADS_KEY), HEADS_KEY);

        List<ConfigurationSection> headsList = (List<ConfigurationSection>) headsYaml.getList(HEADS_KEY);
        return loadHeads(headsList);
    }

    /**
     * Loads all the heads in the provided data, provided it complies with the expected format.
     * @param headsList the head data
     * @return a key:head map of registered heads
     */
    public HashMap<String, IHead> loadHeads(List<ConfigurationSection> headsList)
    {
        HashMap<String, IHead> loadedHeads = new HashMap<>();
        for (ConfigurationSection headData : headsList)
        {
            try
            {
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
            String name = (headYaml.contains(HEAD_KEY_KEY) ? headYaml.getString(HEAD_KEY_KEY) : "[UNKNOWN]");
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
        if (!headYaml.contains(HEAD_KEY_KEY))
            throw new MissingFieldsException(String.format("Missing Field: %s", HEAD_KEY_KEY), HEAD_KEY_KEY);
        if (!headYaml.contains(HEAD_TYPE_KEY))
            throw new MissingFieldsException(String.format("Missing Field: %s", HEAD_TYPE_KEY), HEAD_TYPE_KEY);
        if (!headYaml.contains(HEAD_NAME_KEY))
            throw new MissingFieldsException(String.format("Missing Field: %s", HEAD_NAME_KEY), HEAD_NAME_KEY);

        String key = headYaml.getString(HEAD_KEY_KEY);
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

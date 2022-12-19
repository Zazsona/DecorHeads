package com.zazsona.decorheads.config.load;

import com.zazsona.decorheads.DecorHeadsPlugin;
import com.zazsona.decorheads.headdata.IHead;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import javax.management.openmbean.InvalidKeyException;
import java.util.HashMap;

public class ItemLoader
{
    /**
     * Loads an item from a namespaced key. Supports Minecraft, Bukkit, and DecorHeads namespaces.
     * @param key the namespaced key identifying the item to load
     * @param headMap a map of heads registered with DecorHeads
     * @return the loaded {@link ItemStack}
     * @throws InvalidKeyException no item is registered against the key
     */
    public static ItemStack loadItem(String key, HashMap<String, IHead> headMap) throws InvalidKeyException
    {
        NamespacedKey namespacedKey = NamespacedKey.fromString(key);
        return loadItem(namespacedKey, headMap);
    }

    /**
     * Loads an item from a namespaced key. Supports Minecraft, Bukkit, and DecorHeads namespaces.
     * @param key the namespaced key identifying the item to load
     * @param headMap a map of heads registered with DecorHeads
     * @return the loaded {@link ItemStack}
     * @throws IllegalArgumentException no item is registered against the key
     */
    public static ItemStack loadItem(NamespacedKey key, HashMap<String, IHead> headMap) throws IllegalArgumentException
    {
        // TODO: Potions minecraft:potion{Potion:luck}
        String namespace = key.getNamespace();
        if (namespace.equalsIgnoreCase(NamespacedKey.MINECRAFT) || namespace.equalsIgnoreCase(NamespacedKey.BUKKIT))
        {
            Material material = Material.matchMaterial(key.toString());
            if (material == null)
                throw new InvalidKeyException(String.format("Minecraft item key \"%s\" does not exist.", key.getKey()));
            ItemStack itemStack = new ItemStack(material);
            return itemStack;
        }
        else if (namespace.equalsIgnoreCase(DecorHeadsPlugin.PLUGIN_NAME))
        {
            String headKey = key.getKey();
            IHead head = headMap.get(headKey);
            if (head == null)
                throw new InvalidKeyException(String.format("Head key \"%s\" does not exist.", headKey));
            return head.createItem();
        }
        else
            throw new InvalidKeyException(String.format("Unrecognised namespace: \"%s\"", key.getNamespace()));
    }
}

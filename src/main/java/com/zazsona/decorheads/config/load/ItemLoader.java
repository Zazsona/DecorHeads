package com.zazsona.decorheads.config.load;

import com.zazsona.decorheads.DecorHeadsPlugin;
import com.zazsona.decorheads.headdata.IHead;
import com.zazsona.decorheads.nbt.PotionNBTWrapper;
import net.querz.nbt.io.SNBTUtil;
import net.querz.nbt.tag.CompoundTag;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;

import javax.management.openmbean.InvalidKeyException;
import java.io.IOException;
import java.util.HashMap;

public class ItemLoader
{
    /**
     * Loads an item from a namespaced key. Supports Minecraft, Bukkit, and DecorHeads namespaces.
     * @param itemDefinition the namespaced key identifying the item to load, as well as any SNBT data suffixed. E.g. minecraft:potion{Potion:luck}
     * @param headMap a map of heads registered with DecorHeads
     * @return the loaded {@link ItemStack}
     * @throws InvalidKeyException no item is registered against the key
     */
    public static ItemStack loadItem(String itemDefinition, HashMap<String, IHead> headMap) throws InvalidKeyException, IOException
    {
        String key = itemDefinition;
        String snbt = null;
        if (itemDefinition.contains("{") && itemDefinition.contains("}"))
        {
            key = itemDefinition.substring(0, itemDefinition.indexOf("{"));
            snbt = itemDefinition.substring(itemDefinition.indexOf("{"), itemDefinition.lastIndexOf("}") + 1);
        }

        NamespacedKey namespacedKey = NamespacedKey.fromString(key.toLowerCase());
        return loadItem(namespacedKey, snbt, headMap);
    }

    /**
     * Loads an item from a namespaced key. Supports Minecraft, Bukkit, and DecorHeads namespaces.
     * @param key the namespaced key identifying the item to load
     * @param snbt the snbt data for the item
     * @param headMap a map of heads registered with DecorHeads
     * @return the loaded {@link ItemStack}
     * @throws IllegalArgumentException no item is registered against the key
     */
    private static ItemStack loadItem(NamespacedKey key, String snbt, HashMap<String, IHead> headMap) throws IllegalArgumentException, IOException
    {
        //TODO: Test potion loading - minecraft:potion{Potion:luck}
        String namespace = key.getNamespace();
        if (namespace.equalsIgnoreCase(NamespacedKey.MINECRAFT) || namespace.equalsIgnoreCase(NamespacedKey.BUKKIT))
        {
            Material material = Material.matchMaterial(key.toString());
            if (material == null)
                throw new InvalidKeyException(String.format("Minecraft item key \"%s\" does not exist.", key.getKey()));

            ItemStack itemStack = new ItemStack(material);
            if (snbt == null)
                return itemStack;

            CompoundTag tag = (CompoundTag) SNBTUtil.fromSNBT(snbt);
            if (material == Material.POTION || material == Material.LINGERING_POTION || material == Material.SPLASH_POTION)
            {
                PotionNBTWrapper potionNBT = new PotionNBTWrapper(tag);
                PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
                potionMeta.setBasePotionData(new PotionData(potionNBT.getPotionType()));
                itemStack.setItemMeta(potionMeta);
            }
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

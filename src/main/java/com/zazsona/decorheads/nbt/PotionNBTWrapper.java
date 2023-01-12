package com.zazsona.decorheads.nbt;

import net.querz.nbt.tag.CompoundTag;
import org.bukkit.potion.PotionType;

public class PotionNBTWrapper extends NBTWrapper
{
    public static String POTION_TAG = "Potion";

    public PotionNBTWrapper(CompoundTag tag)
    {
        super(tag);
    }

    public String getPotionId()
    {
        return getTag().getString(POTION_TAG);
    }

    public PotionType getPotionType()
    {
        String id = getPotionId();
        String type = id.substring(id.indexOf("_"), id.length()).toUpperCase();
        return PotionType.valueOf(type);
    }
}

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
        String typeName = getPotionId().toUpperCase()
                .replace("STRONG_", "")
                .replace("LONG_", "");
        return PotionType.valueOf(typeName);
    }

    public boolean isExtended()
    {
        return getPotionId().toUpperCase().startsWith("LONG_");
    }

    public boolean isUpgraded()
    {
        return getPotionId().toUpperCase().startsWith("STRONG_");
    }
}

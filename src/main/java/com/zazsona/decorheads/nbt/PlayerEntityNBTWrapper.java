package com.zazsona.decorheads.nbt;

import net.querz.nbt.tag.CompoundTag;

public class PlayerEntityNBTWrapper extends NBTWrapper
{
    public static String ID_TAG = "Id";

    public PlayerEntityNBTWrapper(CompoundTag tag)
    {
        super(tag);
    }

    public String getId()
    {
        return getTag().getString(ID_TAG);
    }
}

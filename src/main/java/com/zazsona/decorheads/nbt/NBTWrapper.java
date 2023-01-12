package com.zazsona.decorheads.nbt;

import net.querz.nbt.io.SNBTUtil;
import net.querz.nbt.tag.CompoundTag;

import java.io.IOException;

public abstract class NBTWrapper
{
    private CompoundTag tag;

    public NBTWrapper(CompoundTag tag)
    {
        this.tag = tag;
    }

    public NBTWrapper(String tag) throws IOException
    {
        this.tag = (CompoundTag) SNBTUtil.fromSNBT(tag);
    }

    public CompoundTag getTag()
    {
        return tag;
    }
}

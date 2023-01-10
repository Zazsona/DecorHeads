package com.zazsona.decorheads.event;

import org.bukkit.Chunk;
import org.jetbrains.annotations.NotNull;

public class RegionLoadEvent extends RegionEvent
{
    public RegionLoadEvent(@NotNull Chunk triggeringChunk)
    {
        super(triggeringChunk);
    }
}

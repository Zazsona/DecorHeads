package com.zazsona.decorheads.event;

import org.bukkit.Chunk;
import org.jetbrains.annotations.NotNull;

public class RegionUnloadEvent extends RegionEvent
{
    public RegionUnloadEvent(@NotNull Chunk triggeringChunk)
    {
        super(triggeringChunk);
    }
}

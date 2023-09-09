package com.zazsona.decorheads.event;

import org.bukkit.block.Block;
import org.bukkit.metadata.Metadatable;

public interface IBlockBreakByExplosionEvent
{
    Metadatable getExplosionSource();

    Block getBlock();
}

package com.zazsona.decorheads.blockmeta.library.extensions.org.bukkit.Chunk;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import org.bukkit.Chunk;

@Extension
public class ChunkExtension
{
    private static final String REGION_LOCATION_KEY_FORMAT = "{x}.{z}";

    public static String getRegionKey(@This Chunk thiz)
    {
        int regionXVal = (int) Math.floor(thiz.getX() / 32.0);
        int regionZVal = (int) Math.floor(thiz.getZ() / 32.0);
        return String.format(REGION_LOCATION_KEY_FORMAT, regionXVal, regionZVal);
    }
}

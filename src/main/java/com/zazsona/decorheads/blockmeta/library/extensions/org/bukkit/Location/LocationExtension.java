package com.zazsona.decorheads.blockmeta.library.extensions.org.bukkit.Location;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import org.bukkit.Chunk;
import org.bukkit.Location;

@Extension
public class LocationExtension
{
    private static final String REGION_LOCATION_KEY_FORMAT = "{x}.{z}";
    private static final String CHUNK_LOCATION_KEY_FORMAT = "{x}.{z}";

    public static String getRegionKey(@This Location thiz)
    {
        thiz.toVector();
        int regionX = (int) Math.floor(thiz.getX() / 32.0);
        int regionZ = (int) Math.floor(thiz.getZ() / 32.0);
        return String.format(REGION_LOCATION_KEY_FORMAT, regionX, regionZ);
    }


    public static String getChunkKey(@This Location thiz)
    {
        Chunk chunk = thiz.getChunk();
        return String.format(CHUNK_LOCATION_KEY_FORMAT, chunk.getX(), chunk.getZ());
    }
}

package com.zazsona.decorheads.headdata;

public interface IHead
{
    public String getKey();
    public String getName();
    public String getTextureEncoding();

    public boolean isObtainedThroughDrops();
    public boolean isObtainedThroughCreation();
}

package com.zazsona.decorheads.headdata;

public class Head implements IHead
{
    private String key;
    private String name;
    private String texture;

    public Head(String key, String name, String texture)
    {
        this.key = key;
        this.name = name;
        this.texture = texture;
    }

    @Override
    public String getKey()
    {
        return key;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getTextureEncoding()
    {
        return texture;
    }

    @Override
    public boolean isObtainedThroughDrops()
    {
        return false;
    }

    @Override
    public boolean isObtainedThroughCreation()
    {
        return false;
    }
}

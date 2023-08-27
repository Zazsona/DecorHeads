package com.zazsona.decorheads.headdata;

import org.bukkit.inventory.ItemStack;

public class TextureHead extends Head
{
    private String name;
    private String texture;

    public TextureHead(String key, String name, String texture)
    {
        super(key);
        this.name = name;
        this.texture = texture;
    }

    @Override
    public String getName()
    {
        return name;
    }

    /**
     * The head's texture, in base64
     * @return base64 head data string
     */
    public String getTexture()
    {
        return texture;
    }

    @Override
    public ItemStack createItem()
    {
        return createSkull(name, texture);
    }
}

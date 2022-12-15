package com.zazsona.decorheads.headdata;

import org.bukkit.inventory.ItemStack;

public interface IHead
{
    /**
     * A unique identifying key for the head
     * @return the key
     */
    String getKey();

    /**
     * The name of the head, can contain placeholders so should only be used internally
     * @return the name of the head, with placeholders
     */
    String getName();

    /**
     * The name of the head in a human-friendly format. Use this for printing out.
     * @return the name of the head, with any placeholders filled in
     */
    String getPrettyName();

    /**
     * The head's texture, in base64
     * @return base64 head data string
     */
    String getTexture();

    /**
     * Creates an {@link ItemStack} of this head
     * @return the ItemStack
     */
    ItemStack createItem();

}

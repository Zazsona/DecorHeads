package com.zazsona.decorheads.headdata;

import com.zazsona.decorheads.Core;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.security.InvalidParameterException;
import java.util.HashMap;

public abstract class CraftHead extends HeadDecorator
{
    public CraftHead(IHead head)
    {
        super(head);
    }

    public abstract Recipe getRecipe();
}

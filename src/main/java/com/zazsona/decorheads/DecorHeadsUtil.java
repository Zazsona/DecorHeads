package com.zazsona.decorheads;

import java.util.Locale;

public class DecorHeadsUtil
{
    public static String capitaliseName(String name)
    {
        name = name.replace("_", " ");
        String[] words = name.split(" ");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words.length; i++)
        {
            String word = words[i].toLowerCase();
            builder.append(word.substring(0, 1).toUpperCase());
            builder.append(word.substring(1));
            builder.append(" ");
        }
        return builder.toString().trim();
    }
}

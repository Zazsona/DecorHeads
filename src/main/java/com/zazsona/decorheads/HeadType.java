package com.zazsona.decorheads;

public enum HeadType
{
    DECOR,
    PLAYER;

    /**
     * Attempts to match the {@link HeadType} from the provided string by matching the enum value name format
     * @param headType the name of the head type
     * @return the matching {@link HeadType}, or null if no matches found.
     */
    public static HeadType matchHeadType(String headType)
    {
        try
        {
            String formattedName = headType.toUpperCase().replace("-", "_");
            return HeadType.valueOf(formattedName);
        }
        catch (IllegalArgumentException e)
        {
            return null;
        }
    }
}

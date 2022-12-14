package com.zazsona.decorheads.config;

public enum DropType
{
    BLOCK_BREAK,
    BREW,
    SMELT,
    CRAFT,
    ENTITY_DEATH,
    PLAYER_DEATH;

    /**
     * Attempts to match the {@link DropType} from the provided string by matching the enum value name format
     * @param dropType the name of the drop type
     * @return the matching {@link DropType}, or null if no matches found.
     */
    public static DropType matchDropType(String dropType)
    {
        try
        {
            String formattedName = dropType.toUpperCase().replace("-", "_");
            return DropType.valueOf(formattedName);
        }
        catch (IllegalArgumentException e)
        {
            return null;
        }
    }
}

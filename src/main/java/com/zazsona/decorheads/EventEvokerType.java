package com.zazsona.decorheads;

public enum EventEvokerType
{
    ENVIRONMENT,
    PLAYER;

    /**
     * Attempts to match the {@link EventEvokerType} from the provided string by matching the enum value name format
     * @param eventEvoker the name of the event evoker
     * @return the matching {@link EventEvokerType}, or null if no matches found.
     */
    public static EventEvokerType matchDropType(String eventEvoker)
    {
        try
        {
            String formattedName = eventEvoker.toUpperCase().replace("-", "_");
            return EventEvokerType.valueOf(formattedName);
        }
        catch (IllegalArgumentException e)
        {
            return null;
        }
    }
}

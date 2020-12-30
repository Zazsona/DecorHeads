package com.zazsona.decorheads.apiresponse;

public class ProfileResponse
{
    private String id;
    private String name;
    private Property[] properties;

    /**
     * Gets id
     *
     * @return id
     */
    public String getId()
    {
        return id;
    }

    /**
     * Gets name
     *
     * @return name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Gets properties
     *
     * @return properties
     */
    public Property[] getProperties()
    {
        return properties;
    }

    public Property getPropertyByName(String name)
    {
        for (Property property : properties)
        {
            if (property.getName().equalsIgnoreCase(name))
                return property;
        }
        return null;
    }
}

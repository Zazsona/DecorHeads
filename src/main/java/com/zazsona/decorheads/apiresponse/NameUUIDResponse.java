package com.zazsona.decorheads.apiresponse;

public class NameUUIDResponse
{
    private String id;
    private String name;
    private String error;

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

    public boolean isSuccess()
    {
        return error == null;
    }

    /**
     * Gets error
     *
     * @return error
     */
    public String getError()
    {
        return error;
    }
}

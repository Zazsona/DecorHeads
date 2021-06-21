package com.zazsona.decorheads.exceptions;

public class InvalidHeadSourceException extends Exception
{
    public InvalidHeadSourceException()
    {
        super();
    }

    public InvalidHeadSourceException(String message)
    {
        super(message);
    }

    public InvalidHeadSourceException(Throwable cause)
    {
        super(cause);
    }

    public InvalidHeadSourceException(String message, Throwable cause)
    {
        super(message, cause);
    }
}

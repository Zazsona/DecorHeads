package com.zazsona.decorheads.exceptions;

public class InvalidHeadException extends Exception
{
    public InvalidHeadException()
    {
        super();
    }

    public InvalidHeadException(String message)
    {
        super(message);
    }

    public InvalidHeadException(Throwable cause)
    {
        super(cause);
    }

    public InvalidHeadException(String message, Throwable cause)
    {
        super(message, cause);
    }
}

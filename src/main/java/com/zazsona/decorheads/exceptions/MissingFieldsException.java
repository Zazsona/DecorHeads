package com.zazsona.decorheads.exceptions;

import java.util.Arrays;

public class MissingFieldsException extends RuntimeException
{
    private String[] fieldNames;

    public MissingFieldsException()
    {
        super();
    }

    public MissingFieldsException(String message, String... fieldNames)
    {
        super(message);
        this.fieldNames = fieldNames;
    }

    public MissingFieldsException(Throwable cause, String... fieldNames)
    {
        super(cause);
        this.fieldNames = fieldNames;
    }

    public MissingFieldsException(String message, Throwable cause, String... fieldNames)
    {
        super(message, cause);
        this.fieldNames = fieldNames;
    }

    public String[] getFieldNames()
    {
        return Arrays.copyOf(fieldNames, fieldNames.length);
    }
}

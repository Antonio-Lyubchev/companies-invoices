package com.estafet.companies.exception;

public class ApiException extends Exception
{
    public ApiException()
    {
    }

    public ApiException(String message)
    {
        super(message);
    }
}

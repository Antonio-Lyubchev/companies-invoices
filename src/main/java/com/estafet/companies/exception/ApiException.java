package com.estafet.companies.exception;

public class ApiException extends Throwable
{
    public ApiException()
    {
    }

    public ApiException(String message)
    {
        super(message);
    }
}

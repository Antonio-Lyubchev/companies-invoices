package com.estafet.companies.exception;

public class InvalidInputException extends ApiException
{
    public InvalidInputException()
    {
    }

    public InvalidInputException(String message)
    {
        super(message);
    }
}
package com.estafet.companies.exception;

public class EntityNotFoundException extends ApiException
{
    public EntityNotFoundException()
    {
    }

    public EntityNotFoundException(String message)
    {
        super(message);
    }
}
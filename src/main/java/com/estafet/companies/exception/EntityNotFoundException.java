package com.estafet.companies.exception;

public class EntityNotFoundException extends Throwable
{
    public EntityNotFoundException()
    {
    }

    public EntityNotFoundException(String message)
    {
        super(message);
    }
}

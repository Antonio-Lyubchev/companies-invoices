package com.estafet.companies.utils;

import com.estafet.companies.exception.InvalidInputException;
import org.springframework.util.StringUtils;

public class Utils
{
    private Utils()
    {
    }

    public static long parseAndValidate(String taxIdStr) throws InvalidInputException
    {
        if (!StringUtils.hasText(taxIdStr))
        {
            throw new InvalidInputException("Company tax ID is invalid!");
        }

        long parsedTaxId;
        try
        {
            parsedTaxId = Long.parseLong(taxIdStr);
        } catch (NumberFormatException ex)
        {
            throw new InvalidInputException("Company tax ID is invalid!");
        }

        return parsedTaxId;
    }
}

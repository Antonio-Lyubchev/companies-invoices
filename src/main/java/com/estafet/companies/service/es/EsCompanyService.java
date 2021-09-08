package com.estafet.companies.service.es;

import com.estafet.companies.exception.EntityNotFoundException;
import com.estafet.companies.exception.InvalidInputException;
import com.estafet.companies.model.es.EsCompany;

import java.util.List;

public interface EsCompanyService
{
    EsCompany getCompany(long taxId) throws InvalidInputException, EntityNotFoundException;

    long addCompany(EsCompany newCompany) throws InvalidInputException;

    void updateCompany(long taxId, EsCompany newCompany) throws InvalidInputException;

    void addCompanies(List<EsCompany> companies) throws InvalidInputException;

    void deleteCompany(long taxId) throws InvalidInputException, EntityNotFoundException;

    Iterable<EsCompany> getAllCompanies();
}

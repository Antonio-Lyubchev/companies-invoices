package com.estafet.companies.service;

import com.estafet.companies.exception.EntityNotFoundException;
import com.estafet.companies.exception.InvalidInputException;
import com.estafet.companies.model.Company;

import java.util.List;

public interface CompanyService
{
    Company getCompany(String taxId) throws InvalidInputException, EntityNotFoundException;

    String addCompany(Company newCompany) throws InvalidInputException;

    void updateCompany(String taxId, Company newCompany) throws InvalidInputException;

    void addCompanies(List<Company> companies) throws InvalidInputException;

    void deleteCompany(String taxId) throws InvalidInputException, EntityNotFoundException;

    List<Company> getAllCompanies();
}

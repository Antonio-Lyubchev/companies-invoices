package com.estafet.companies.service;

import com.estafet.companies.model.Company;

import java.util.List;

public interface CompanyService
{
    Company getCompany(String taxId);

    String addCompany(Company newCompany);

    void addCompanies(List<Company> companies);

    void updateCompany(String taxId, Company newCompany);

    void deleteCompany(String taxId);

    List<Company> getAllCompanies();
}

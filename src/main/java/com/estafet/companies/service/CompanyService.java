package com.estafet.companies.service;

import com.estafet.companies.exception.ApiException;
import com.estafet.companies.exception.EntityNotFoundException;
import com.estafet.companies.exception.InvalidInputException;
import com.estafet.companies.model.Company;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;

@Service
public class CompanyService
{
    private final HashMap<String, Company> companyMap = new HashMap<>()
    {
        {
            put("taxId1", new Company("company1", "taxId1", "addr1", "repr1"));
            put("taxId2", new Company("company2", "taxId2", "addr2", "repr2"));
            put("taxId3", new Company("company3", "taxId3", "addr3", "repr3"));
        }
    };

    public HashMap<String, Company> getCompanyMap()
    {
        return companyMap;
    }

    public Company getCompany(String taxId) throws EntityNotFoundException, InvalidInputException
    {
        if (!StringUtils.hasText(taxId))
        {
            throw new InvalidInputException("Company tax ID is invalid!");
        }

        Company resultCompany = companyMap.getOrDefault(taxId, null);
        if (resultCompany == null)
        {
            throw new EntityNotFoundException("Company with tax ID: '" + taxId + "' not found!");
        }

        return resultCompany;
    }

    public String addCompany(Company company) throws InvalidInputException
    {
        if (company == null)
        {
            throw new InvalidInputException("Company is invalid!");
        }

        // post is NOT idempotent, but more than 1 entry of the same company is illogical
        if (companyMap.containsKey(company.getTaxId()))
        {
            throw new InvalidInputException("Tried to add an existing company!");
        }

        return companyMap.put(company.getTaxId(), company).getTaxId();
    }

    public void addCompanies(List<Company> companies) throws InvalidInputException
    {
        for (Company c : companies)
        {
            addCompany(c);
        }
    }

    // TODO: what will happen if taxId and taxId from company object differ?
    public void updateCompany(String taxId, Company updatedCompany) throws InvalidInputException
    {
        if (!StringUtils.hasText(taxId))
        {
            throw new InvalidInputException("Company tax ID is invalid!");
        }

        if (updatedCompany == null)
        {
            throw new InvalidInputException("Company is invalid!");
        }

        companyMap.put(taxId, updatedCompany);
    }

    public void deleteCompany(String taxId) throws ApiException
    {
        if (!StringUtils.hasText(taxId))
        {
            throw new InvalidInputException("Company tax ID is invalid!");
        }

        if (companyMap.remove(taxId) == null)
        {
            throw new EntityNotFoundException("Tried to remove a non-existing company");
        }
    }
}

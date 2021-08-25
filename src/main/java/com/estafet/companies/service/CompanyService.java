package com.estafet.companies.service;

import com.estafet.companies.exception.ApiException;
import com.estafet.companies.exception.EntityNotFoundException;
import com.estafet.companies.exception.InvalidInputException;
import com.estafet.companies.model.Company;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.IntStream;

@Service
public class CompanyService
{
    private final List<Company> companyList;

    public CompanyService()
    {
        companyList = new ArrayList<>();
    }

    public Company getCompany(String taxId) throws EntityNotFoundException, InvalidInputException
    {
        if (!StringUtils.hasText(taxId))
        {
            throw new InvalidInputException("Company tax ID is invalid!");
        }

        return companyList.stream()
                .filter(c -> c.getTaxId().equals(taxId))
                .findAny()
                .orElseThrow(() -> new EntityNotFoundException("Company with tax ID: '" + taxId + "' not found!"));
    }

    public String addCompany(Company newCompany) throws InvalidInputException
    {
        if (newCompany == null)
        {
            throw new InvalidInputException("Company is invalid!");
        }

        Optional<Company> company = companyList.stream()
                .filter(c -> c.getTaxId().equals(newCompany.getTaxId()))
                .findAny();

        if (company.isPresent())
        {
            throw new InvalidInputException("Tried to add an existing company!");
        }

        companyList.add(newCompany);

        return newCompany.getTaxId();
    }

    public void addCompanies(List<Company> companies) throws InvalidInputException
    {
        for (Company c : companies)
        {
            addCompany(c);
        }
    }

    public void updateCompany(String taxId, Company newCompany) throws InvalidInputException
    {
        if (!StringUtils.hasText(taxId))
        {
            throw new InvalidInputException("Company tax ID is invalid!");
        }

        if (newCompany == null)
        {
            throw new InvalidInputException("Company is invalid!");
        }

        int indexToUpdate = IntStream.range(0, companyList.size())
                .filter(i -> companyList.get(i).getTaxId().equals(taxId))
                .findFirst()
                .orElse(-1);

        if (indexToUpdate != -1)
        {
            companyList.set(indexToUpdate, newCompany);
        } else
        {
            companyList.add(newCompany);
        }
    }

    public void deleteCompany(String taxId) throws ApiException
    {
        if (!StringUtils.hasText(taxId))
        {
            throw new InvalidInputException("Company tax ID is invalid!");
        }

        // if no elements got removed, throw so we can return 404
        if (!companyList.removeIf(c -> c.getTaxId().equals(taxId)))
        {
            throw new EntityNotFoundException("Tried to remove a non-existing company");
        }
    }

    public boolean isCompanyExistent(String taxId)
    {
        Optional<Company> company = companyList.stream()
                .filter(c -> c.getTaxId().equals(taxId))
                .findAny();

        return company.isPresent();
    }

    public List<Company> getAllCompanies()
    {
        return Collections.unmodifiableList(companyList);
    }
}

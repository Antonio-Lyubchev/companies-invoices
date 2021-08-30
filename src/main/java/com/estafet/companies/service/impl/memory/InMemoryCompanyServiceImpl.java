package com.estafet.companies.service.impl.memory;

import com.estafet.companies.exception.EntityNotFoundException;
import com.estafet.companies.exception.InvalidInputException;
import com.estafet.companies.model.Company;
import com.estafet.companies.service.CompanyService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
@Deprecated
public class InMemoryCompanyServiceImpl implements CompanyService
{
    private final List<Company> companyList;

    public InMemoryCompanyServiceImpl()
    {
        companyList = new ArrayList<>();
    }

    @Override
    public Company getCompany(String taxId) throws InvalidInputException, EntityNotFoundException
    {
        if (!StringUtils.hasText(taxId))
        {
            throw new InvalidInputException("Company tax ID is invalid!");
        }

        return companyList.stream()
                .filter(c -> c.getTaxNumber().equals(taxId))
                .findAny()
                .orElseThrow(() -> new EntityNotFoundException("Company with tax ID: '" + taxId + "' not found!"));
    }

    @Override
    public String addCompany(Company newCompany) throws InvalidInputException
    {
        if (newCompany == null)
        {
            throw new InvalidInputException("Company is invalid!");
        }

        Optional<Company> company = companyList.stream()
                .filter(c -> c.getTaxNumber().equals(newCompany.getTaxNumber()))
                .findAny();

        if (company.isPresent())
        {
            throw new InvalidInputException("Tried to add an existing company!");
        }

        companyList.add(newCompany);

        return newCompany.getTaxNumber();
    }

    public void addCompanies(List<Company> companies) throws InvalidInputException
    {
        for (Company c : companies)
        {
            addCompany(c);
        }
    }

    @Override
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
                .filter(i -> companyList.get(i).getTaxNumber().equals(taxId))
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

    @Override
    public void deleteCompany(String taxId) throws InvalidInputException, EntityNotFoundException
    {
        if (!StringUtils.hasText(taxId))
        {
            throw new InvalidInputException("Company tax ID is invalid!");
        }

        // if no elements got removed, throw so we can return 404
        if (!companyList.removeIf(c -> c.getTaxNumber().equals(taxId)))
        {
            throw new EntityNotFoundException("Tried to remove a non-existing company");
        }
    }

    public boolean isCompanyExistent(Company companyToLookFor)
    {
        Optional<Company> company = companyList.stream()
                .filter(c -> c.getTaxNumber().equals(companyToLookFor.getTaxNumber()))
                .findAny();

        return company.isPresent();
    }

    @Override
    public List<Company> getAllCompanies()
    {
        return Collections.unmodifiableList(companyList);
    }
}

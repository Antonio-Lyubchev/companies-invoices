package com.estafet.companies.service.impl.db;

import com.estafet.companies.exception.EntityNotFoundException;
import com.estafet.companies.exception.InvalidInputException;
import com.estafet.companies.model.Company;
import com.estafet.companies.repository.jpa.CompanyRepository;
import com.estafet.companies.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Primary
@Service
public class DbCompanyServiceImpl implements CompanyService
{
    @Autowired
    private CompanyRepository repository;

    @Override
    public Company getCompany(long taxId) throws EntityNotFoundException
    {
        Optional<Company> company = repository.findById(taxId);
        if (company.isEmpty())
        {
            throw new EntityNotFoundException("Company with taxId '" + taxId + "' not found!");
        }

        return company.get();
    }

    @Override
    public long addCompany(Company newCompany)
    {
        repository.save(newCompany);
        return newCompany.getTaxNumber();
    }

    @Override
    public void addCompanies(List<Company> companies)
    {
        repository.saveAll(companies);
    }

    @Override
    public void updateCompany(long taxId, Company newCompany) throws InvalidInputException
    {
        if (taxId != newCompany.getTaxNumber())
        {
            throw new InvalidInputException("You cannot change the tax number of an existing company!");
        }

        repository.save(newCompany);
    }

    @Override
    public void deleteCompany(long taxId) throws EntityNotFoundException
    {
        try
        {
            repository.deleteById(taxId);
        } catch (EmptyResultDataAccessException ex)
        {
            throw new EntityNotFoundException("Company with tax number '" + taxId + "' does not exist!");
        }
    }

    @Override
    public List<Company> getAllCompanies()
    {
        return repository.findAll();
    }
}

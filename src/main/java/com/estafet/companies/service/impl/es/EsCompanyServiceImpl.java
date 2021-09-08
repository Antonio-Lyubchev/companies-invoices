package com.estafet.companies.service.impl.es;

import com.estafet.companies.exception.EntityNotFoundException;
import com.estafet.companies.exception.InvalidInputException;
import com.estafet.companies.model.es.EsCompany;
import com.estafet.companies.repository.es.CompanyRepositoryEs;
import com.estafet.companies.service.es.EsCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EsCompanyServiceImpl implements EsCompanyService
{
    @Autowired
    private CompanyRepositoryEs repository;

    @Override
    public EsCompany getCompany(long taxId) throws InvalidInputException, EntityNotFoundException
    {
        if (taxId <= 0) throw new InvalidInputException("Company tax id must be positive");

        Optional<EsCompany> companyOptional = repository.findById(taxId);

        return companyOptional.orElseThrow(() -> new EntityNotFoundException("Company with id: " + taxId + " not found"));
    }

    @Override
    public long addCompany(EsCompany newCompany) throws InvalidInputException
    {
        if (newCompany == null) throw new InvalidInputException("Company cannot be null");

        return repository.save(newCompany).getTaxNumber();
    }

    @Override
    public void updateCompany(long taxId, EsCompany newCompany) throws InvalidInputException
    {
        if (taxId != newCompany.getTaxNumber())
        {
            throw new InvalidInputException("You cannot change the tax number of an existing company!");
        }

        repository.save(newCompany);
    }

    @Override
    public void addCompanies(List<EsCompany> companies)
    {
        repository.saveAll(companies);
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
    public Iterable<EsCompany> getAllCompanies()
    {
        return repository.findAll();
    }
}

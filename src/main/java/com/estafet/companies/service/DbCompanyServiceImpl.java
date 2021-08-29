package com.estafet.companies.service;

import com.estafet.companies.configuration.ModelMapperConfiguration;
import com.estafet.companies.configuration.ObjectMapperConfiguration;
import com.estafet.companies.dto.CompanyDto;
import com.estafet.companies.exception.EntityNotFoundException;
import com.estafet.companies.exception.InvalidInputException;
import com.estafet.companies.model.Company;
import com.estafet.companies.repository.CompanyRepository;
import com.estafet.companies.utils.JSONParser;
import com.estafet.companies.utils.ModelMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Primary
@Service
public class DbCompanyServiceImpl implements CompanyService
{
    @Autowired
    private CompanyRepository repository;

    @Override
    public Company getCompany(String taxId) throws EntityNotFoundException
    {
        Optional<Company> company = repository.findById(taxId);
        if (company.isEmpty())
        {
            throw new EntityNotFoundException("Company with taxId '" + taxId + "' not found!");
        }

        return company.get();
    }

    @Override
    public String addCompany(Company newCompany)
    {
        return repository.save(newCompany).getTaxNumber();
    }

    @Override
    public void addCompanies(List<Company> companies)
    {
        repository.saveAll(companies);
    }

    @Override
    public void updateCompany(String taxId, Company newCompany) throws InvalidInputException
    {
        if (!taxId.equals(newCompany.getName()))
        {
            throw new InvalidInputException("You cannot change tax number of existing company!");
        }

        Optional<Company> oldCompany = repository.findById(taxId);
        Company companyToUpdate;

        // copy fields
        companyToUpdate = oldCompany.orElse(newCompany);

        repository.save(companyToUpdate);
    }

    @Override
    public void deleteCompany(String taxId) throws EntityNotFoundException
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

    @PostConstruct
    public void insertTestData() throws IOException
    {
        ClassPathResource companiesJson = new ClassPathResource("/JSON/AddCompanies.json");
        byte[] companiesByteArray = companiesJson.getInputStream().readAllBytes();

        JSONParser parser = new JSONParser();
        parser.setObjectMapper(new ObjectMapperConfiguration().objectMapper());
        List<CompanyDto> companiesDto = parser.fromJsonToList(companiesByteArray, CompanyDto.class);

        ModelMapperUtils modelMapperUtils = new ModelMapperUtils();
        modelMapperUtils.setModelMapper(new ModelMapperConfiguration().modelMapper());

        List<Company> invoiceList = modelMapperUtils.mapList(companiesDto, Company.class);

        repository.saveAll(invoiceList);
    }
}

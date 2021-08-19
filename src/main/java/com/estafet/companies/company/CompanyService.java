package com.estafet.companies.company;

import com.estafet.companies.exception.ApiException;
import com.estafet.companies.exception.EntityNotFoundException;
import com.estafet.companies.exception.InvalidInputException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;

@Service
public class CompanyService
{
    private final HashMap<String, Company> companyMap = new HashMap<>()
    {
        {
            put("company1", new Company("company1", "tax1", "addr1", "repr1"));
            put("company2", new Company("company2", "tax2", "addr2", "repr2"));
            put("company3", new Company("company3", "tax3", "addr3", "repr3"));
        }
    };

    public HashMap<String, Company> getCompanyMap()
    {
        return companyMap;
    }

    public Company getCompany(String name) throws EntityNotFoundException, InvalidInputException
    {
        if (!StringUtils.hasText(name))
        {
            throw new InvalidInputException("Company name is invalid!");
        }

        Company resultCompany = companyMap.getOrDefault(name, null);
        if (resultCompany == null)
        {
            throw new EntityNotFoundException("Company '" + name + "' not found!");
        }

        return resultCompany;
    }

    public void addCompany(Company company) throws InvalidInputException
    {
        if (company == null)
        {
            throw new InvalidInputException("Company is invalid!");
        }
        companyMap.put(company.getName(), company);
    }

    public void updateCompany(String name, Company updatedCompany) throws InvalidInputException
    {
        if (!StringUtils.hasText(name))
        {
            throw new InvalidInputException("Company name is invalid!");
        }

        if (updatedCompany == null)
        {
            throw new InvalidInputException("Company is invalid!");
        }

        // remove the old one as we might have changed the name, which means that the key changes as well
        companyMap.remove(name);
        companyMap.put(updatedCompany.getName(), updatedCompany);
    }

    public void deleteCompany(String name) throws ApiException
    {
        if (!StringUtils.hasText(name))
        {
            throw new InvalidInputException("Company name is invalid!");
        }

        if (companyMap.remove(name) == null)
        {
            throw new ApiException("Tried to remove a non-existing company");
        }
    }
}

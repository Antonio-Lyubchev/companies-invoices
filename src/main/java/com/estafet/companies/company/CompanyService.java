package com.estafet.companies.company;

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

    public Company getCompany(String name)
    {
        return companyMap.getOrDefault(name, null);
    }

    public void addCompany(Company company)
    {
        if (company != null)
        {
            companyMap.put(company.getName(), company);
        }
    }

    public void updateCompany(String name, Company updatedCompany)
    {
        if (StringUtils.hasText(name) && updatedCompany != null)
        {
            // remove the old one as we might have changed the name, which means that the key changes as well
            companyMap.remove(name);
            companyMap.put(updatedCompany.getName(), updatedCompany);
        }
    }

    public void deleteCompany(String name)
    {
        if (StringUtils.hasText(name))
        {
            companyMap.remove(name);
        }
    }
}

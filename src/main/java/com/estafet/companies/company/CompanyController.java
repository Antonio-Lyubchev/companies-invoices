package com.estafet.companies.company;

import com.estafet.companies.exception.EntityNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class CompanyController
{
    private final CompanyService companyService;

    CompanyController(CompanyService companyService)
    {
        this.companyService = companyService;
    }

    @GetMapping("/companies")
    public HashMap<String, Company> getAllCompanies()
    {
        return companyService.getCompanyMap();
    }

    @GetMapping("/companies/{name}")
    public Company getCompanyByName(@PathVariable("name") String name) throws EntityNotFoundException
    {
        Company resultCompany = companyService.getCompany(name);
        if (resultCompany == null)
        {
            throw new EntityNotFoundException("Company not found: " + name);
        }

        return resultCompany;
    }

    @PostMapping("/companies")
    public void addCompany(@RequestBody Company company)
    {
        companyService.addCompany(company);
    }

    @PutMapping("/companies/{name}")
    public void updateCompany(@PathVariable("name") String name, @RequestBody Company company)
    {
        companyService.updateCompany(name, company);
    }

    @DeleteMapping("/companies/{name}")
    public void deleteCompany(@PathVariable("name") String name)
    {
        companyService.deleteCompany(name);
    }
}

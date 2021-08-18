package com.estafet.companies.company;

import com.estafet.companies.exception.ApiException;
import com.estafet.companies.exception.EntityNotFoundException;
import com.estafet.companies.exception.InvalidInputException;
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
    public Company getCompanyByName(@PathVariable("name") String name) throws EntityNotFoundException, InvalidInputException
    {
        return companyService.getCompany(name);
    }

    @PostMapping("/companies")
    public void addCompany(@RequestBody Company company)
    {
        companyService.addCompany(company);
    }

    @PutMapping("/companies/{name}")
    public void updateCompany(@PathVariable("name") String name, @RequestBody Company company) throws InvalidInputException
    {
        companyService.updateCompany(name, company);
    }

    @DeleteMapping("/companies/{name}")
    public void deleteCompany(@PathVariable("name") String name) throws ApiException
    {
        companyService.deleteCompany(name);
    }
}

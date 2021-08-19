package com.estafet.companies.controller;

import com.estafet.companies.model.Company;
import com.estafet.companies.service.CompanyService;
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

    @GetMapping("/companies/{id}")
    public Company getCompanyByName(@PathVariable("id") String taxId) throws EntityNotFoundException, InvalidInputException
    {
        return companyService.getCompany(taxId);
    }

    @PostMapping("/companies")
    public void addCompany(@RequestBody Company company) throws InvalidInputException
    {
        companyService.addCompany(company);
    }

    @PutMapping("/companies/{id}")
    public void updateCompany(@PathVariable("id") String taxId, @RequestBody Company company) throws InvalidInputException
    {
        companyService.updateCompany(taxId, company);
    }

    @DeleteMapping("/companies/{id}")
    public void deleteCompany(@PathVariable("id") String taxId) throws ApiException
    {
        companyService.deleteCompany(taxId);
    }
}

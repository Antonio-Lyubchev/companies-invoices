package com.estafet.companies.controller;

import com.estafet.companies.exception.ApiException;
import com.estafet.companies.exception.EntityNotFoundException;
import com.estafet.companies.exception.InvalidInputException;
import com.estafet.companies.model.Company;
import com.estafet.companies.service.CompanyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CompanyController
{
    private final CompanyService companyService;

    CompanyController(CompanyService companyService)
    {
        this.companyService = companyService;
    }

    @GetMapping("/companies")
    public List<Company> getAllCompanies()
    {
        return new ArrayList<>(companyService.getCompanyMap().values());
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

    @PostMapping(path = "/companies", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void saveCompany(@RequestPart MultipartFile file) throws IOException, InvalidInputException
    {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        CollectionType collectionType = typeFactory.constructCollectionType(List.class, Company.class);

        List<Company> companies = mapper.readValue(file.getBytes(), collectionType);

        companyService.addCompanies(companies);
    }
}

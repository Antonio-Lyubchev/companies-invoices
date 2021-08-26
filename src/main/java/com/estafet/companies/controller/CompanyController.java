package com.estafet.companies.controller;

import com.estafet.companies.dto.CompanyDto;
import com.estafet.companies.exception.InvalidInputException;
import com.estafet.companies.model.Company;
import com.estafet.companies.service.CompanyService;
import com.estafet.companies.utils.JSONParser;
import com.estafet.companies.utils.ModelMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CompanyController
{
    private final CompanyService companyService;
    private final JSONParser parser;
    private final ModelMapperUtils modelMapperUtils;

    @Autowired
    CompanyController(CompanyService companyService, JSONParser parser, ModelMapperUtils modelMapperUtils)
    {
        this.companyService = companyService;
        this.parser = parser;
        this.modelMapperUtils = modelMapperUtils;
    }

    @GetMapping("/companies")
    public List<CompanyDto> getAllCompanies()
    {
        List<Company> companiesList = companyService.getAllCompanies();
        return companiesList.stream().map(modelMapperUtils::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/companies/{id}")
    public CompanyDto getCompanyByName(@PathVariable("id") String taxId)
    {
        return modelMapperUtils.convertToDto(companyService.getCompany(taxId));
    }

    @PutMapping("/companies")
    public String addCompany(@RequestBody CompanyDto companyDto)
    {
        Company company = modelMapperUtils.convertToEntity(companyDto);
        return companyService.addCompany(company);
    }

    @PostMapping("/companies/{id}")
    public void updateCompany(@PathVariable("id") String taxId, @RequestBody CompanyDto companyDto)
    {
        companyService.updateCompany(taxId, modelMapperUtils.convertToEntity(companyDto));
    }

    @DeleteMapping("/companies/{id}")
    public void deleteCompany(@PathVariable("id") String taxId)
    {
        companyService.deleteCompany(taxId);
    }

    @PostMapping(path = "/companies", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void addCompany(@RequestPart MultipartFile file) throws IOException, InvalidInputException
    {
        List<CompanyDto> companiesDto = parser.fromJsonToList(file.getBytes(), CompanyDto.class);

        List<Company> invoiceList = modelMapperUtils.mapList(companiesDto, Company.class);

        companyService.addCompanies(invoiceList);
    }

}

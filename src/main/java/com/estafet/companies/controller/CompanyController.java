package com.estafet.companies.controller;

import com.estafet.companies.dto.CompanyDto;
import com.estafet.companies.exception.ApiException;
import com.estafet.companies.exception.EntityNotFoundException;
import com.estafet.companies.exception.InvalidInputException;
import com.estafet.companies.model.Company;
import com.estafet.companies.service.CompanyService;
import com.estafet.companies.utils.JSONParser;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @Autowired
    CompanyController(CompanyService companyService, JSONParser parser, ModelMapper modelMapper)
    {
        this.companyService = companyService;
        this.parser = parser;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/companies")
    public List<CompanyDto> getAllCompanies()
    {
        List<Company> companiesList = companyService.getAllCompanies();

        return companiesList.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/companies/{id}")
    public CompanyDto getCompanyByName(@PathVariable("id") String taxId) throws EntityNotFoundException, InvalidInputException
    {
        return convertToDto(companyService.getCompany(taxId));
    }

    @PutMapping("/companies")
    public String addCompany(@RequestBody CompanyDto companyDto) throws InvalidInputException
    {
        Company company = convertToEntity(companyDto);
        return companyService.addCompany(company);
    }

    @PostMapping("/companies/{id}")
    public void updateCompany(@PathVariable("id") String taxId, @RequestBody CompanyDto companyDto) throws InvalidInputException
    {
        Company company = convertToEntity(companyDto);
        companyService.updateCompany(taxId, company);
    }

    @DeleteMapping("/companies/{id}")
    public void deleteCompany(@PathVariable("id") String taxId) throws ApiException
    {
        companyService.deleteCompany(taxId);
    }

    @PostMapping(path = "/companies", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void addCompany(@RequestPart MultipartFile file) throws IOException, InvalidInputException
    {
        List<Company> companies = parser.fromJsonToList(file.getBytes(), Company.class);

        companyService.addCompanies(companies);
    }

    private CompanyDto convertToDto(Company company)
    {
        return modelMapper.map(company, CompanyDto.class);
    }

    private Company convertToEntity(CompanyDto companyDto)
    {
        return modelMapper.map(companyDto, Company.class);
    }
}

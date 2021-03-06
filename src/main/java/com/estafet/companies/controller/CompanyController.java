package com.estafet.companies.controller;

import com.estafet.companies.dto.CompanyDto;
import com.estafet.companies.exception.EntityNotFoundException;
import com.estafet.companies.exception.InvalidInputException;
import com.estafet.companies.model.Company;
import com.estafet.companies.service.CompanyService;
import com.estafet.companies.utils.JSONParser;
import com.estafet.companies.utils.ModelMapperUtils;
import com.estafet.companies.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
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

    @GetMapping(value = "/companies", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CompanyDto> getAllCompanies()
    {
        List<Company> companiesList = companyService.getAllCompanies();
        return companiesList.stream().map(modelMapperUtils::convertToDto).collect(Collectors.toList());
    }

    @GetMapping(value = "/companies/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CompanyDto getCompany(@PathVariable("id") String taxId) throws InvalidInputException, EntityNotFoundException
    {
        Company company = companyService.getCompany(Utils.parseAndValidate(taxId));
        return modelMapperUtils.convertToDto(company);
    }

    @PutMapping(value = "/companies", produces = MediaType.APPLICATION_JSON_VALUE)
    public CompanyDto addCompany(@Valid @RequestBody CompanyDto companyDto) throws InvalidInputException, EntityNotFoundException
    {
        Company company = modelMapperUtils.convertToEntity(companyDto);
        long companyId = companyService.addCompany(company);
        return modelMapperUtils.convertToDto(companyService.getCompany(companyId));
    }

    @PostMapping(value = "/companies/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateCompany(@PathVariable("id") String taxId, @Valid @RequestBody CompanyDto companyDto) throws InvalidInputException
    {
        companyService.updateCompany(Utils.parseAndValidate(taxId), modelMapperUtils.convertToEntity(companyDto));
    }

    @DeleteMapping(value = "/companies/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteCompany(@PathVariable("id") String taxId) throws InvalidInputException, EntityNotFoundException
    {
        companyService.deleteCompany(Utils.parseAndValidate(taxId));
    }

    @PostMapping(path = "/companies", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void addCompany(@RequestPart MultipartFile file) throws IOException, InvalidInputException
    {
        List<CompanyDto> companiesDto = parser.fromJsonToList(file.getBytes(), CompanyDto.class);

        List<Company> companyList = modelMapperUtils.convertCompanyDtoListToEntity(companiesDto);

        companyService.addCompanies(companyList);
    }
}

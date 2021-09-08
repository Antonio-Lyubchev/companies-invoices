package com.estafet.companies.controller.es;

import com.estafet.companies.dto.CompanyDto;
import com.estafet.companies.exception.EntityNotFoundException;
import com.estafet.companies.exception.InvalidInputException;
import com.estafet.companies.model.es.EsCompany;
import com.estafet.companies.service.es.EsCompanyService;
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

@RestController
@RequestMapping("/es")
public class EsCompanyController
{
    private final EsCompanyService companyService;
    private final JSONParser parser;
    private final ModelMapperUtils modelMapperUtils;

    @Autowired
    EsCompanyController(EsCompanyService companyService, JSONParser parser, ModelMapperUtils modelMapperUtils)
    {
        this.companyService = companyService;
        this.parser = parser;
        this.modelMapperUtils = modelMapperUtils;
    }

    @GetMapping(value = "/companies", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CompanyDto> getAllCompanies()
    {
        return modelMapperUtils.convertCompanyListToDto(companyService.getAllCompanies());
    }

    @GetMapping(value = "/companies/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CompanyDto getCompany(@PathVariable("id") String taxId) throws InvalidInputException, EntityNotFoundException
    {
        return modelMapperUtils.convertToDto(companyService.getCompany(Utils.parseAndValidate(taxId)));
    }

    @PutMapping(value = "/companies", produces = MediaType.APPLICATION_JSON_VALUE)
    public CompanyDto addCompany(@Valid @RequestBody CompanyDto companyDto) throws InvalidInputException
    {
        companyService.addCompany(modelMapperUtils.convertToEsEntity(companyDto));

        return companyDto;
    }

    @PostMapping(value = "/companies/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateCompany(@PathVariable("id") String taxId, @Valid @RequestBody CompanyDto companyDto) throws InvalidInputException
    {
        companyService.updateCompany(Utils.parseAndValidate(taxId), modelMapperUtils.convertToEsEntity(companyDto));
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

        List<EsCompany> companyList = modelMapperUtils.convertEsCompanyDtoListToEntity(companiesDto);

        companyService.addCompanies(companyList);
    }
}

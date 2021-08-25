package com.estafet.companies.controller;

import com.estafet.companies.dto.CompanyDto;
import com.estafet.companies.model.Company;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CompanyDtoTest
{
    private final ModelMapper modelMapper = new ModelMapper();

    @Test
    public void whenConvertCompanyEntityToCompanyDto_thenCorrect()
    {
        Company company = new Company("company4", "taxId4", "addr4", "repr4");

        CompanyDto companyDto = modelMapper.map(company, CompanyDto.class);

        assertEquals(company.getTaxId(), companyDto.getTaxId());
        assertEquals(company.getAddress(), companyDto.getAddress());
        assertEquals(company.getName(), companyDto.getName());
        assertEquals(company.getRepresentative(), companyDto.getRepresentative());
    }

    @Test
    public void whenConvertCompanyDtoToCompanyEntity_thenCorrect()
    {
        CompanyDto companyDto = new CompanyDto("company4", "taxId4", "addr4", "repr4");

        Company company = modelMapper.map(companyDto, Company.class);

        assertEquals(companyDto.getTaxId(), company.getTaxId());
        assertEquals(companyDto.getAddress(), company.getAddress());
        assertEquals(companyDto.getName(), company.getName());
        assertEquals(companyDto.getRepresentative(), company.getRepresentative());
    }
}

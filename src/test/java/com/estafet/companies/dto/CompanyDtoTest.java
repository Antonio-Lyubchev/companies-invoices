package com.estafet.companies.dto;

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
        Company company = new Company(242421L, "taxId4", "addr4", "repr4");

        CompanyDto companyDto = modelMapper.map(company, CompanyDto.class);

        assertEquals(company.getTaxNumber(), companyDto.getTaxNumber());
        assertEquals(company.getAddress(), companyDto.getAddress());
        assertEquals(company.getName(), companyDto.getName());
        assertEquals(company.getRepresentative(), companyDto.getRepresentative());
    }

    @Test
    public void whenConvertCompanyDtoToCompanyEntity_thenCorrect()
    {
        CompanyDto companyDto = new CompanyDto(324234L, "taxId4", "addr4", "wrwe");

        Company company = modelMapper.map(companyDto, Company.class);

        assertEquals(companyDto.getTaxNumber(), company.getTaxNumber());
        assertEquals(companyDto.getAddress(), company.getAddress());
        assertEquals(companyDto.getName(), company.getName());
        assertEquals(companyDto.getRepresentative(), company.getRepresentative());
    }
}

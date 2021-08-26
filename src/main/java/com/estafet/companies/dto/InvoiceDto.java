package com.estafet.companies.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

public class InvoiceDto
{
    private LocalDateTime dateIssued;
    private LocalDateTime dateDue;
    @JsonProperty("company")
    private CompanyDto companyDto;
    @JsonProperty("products")
    private List<InvoiceProductDto> productsDto;

    public InvoiceDto()
    {
    }

    public InvoiceDto(LocalDateTime dateIssued, LocalDateTime dateDue, CompanyDto companyDto, List<InvoiceProductDto> productsDto)
    {
        this.dateIssued = dateIssued;
        this.dateDue = dateDue;
        this.companyDto = companyDto;
        this.productsDto = productsDto;
    }

    public LocalDateTime getDateIssued()
    {
        return dateIssued;
    }

    public void setDateIssued(LocalDateTime dateIssued)
    {
        this.dateIssued = dateIssued;
    }

    public LocalDateTime getDateDue()
    {
        return dateDue;
    }

    public void setDateDue(LocalDateTime dateDue)
    {
        this.dateDue = dateDue;
    }

    public CompanyDto getCompanyDto()
    {
        return companyDto;
    }

    public void setCompanyDto(CompanyDto companyDto)
    {
        this.companyDto = companyDto;
    }

    public List<InvoiceProductDto> getProductsDto()
    {
        return productsDto;
    }

    public void setProductsDto(List<InvoiceProductDto> productsDto)
    {
        this.productsDto = productsDto;
    }
}

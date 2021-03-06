package com.estafet.companies.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDto
{
    // Id will be auto generated by db, so don't NotNull it
    private Long invoiceId;
    @NotNull(message = "Invoice issue date is required")
    private LocalDateTime dateIssued;
    @NotNull(message = "Invoice due date is required")
    private LocalDateTime dateDue;
    @JsonProperty("company")
    @NotNull(message = "Company is required")
    // We don't annotate with @Valid since when deserializing, an invoice could have only company id with it, not a full company object
    private CompanyDto companyDto;
    @Valid
    @JsonProperty("products")
    @NotEmpty(message = "Product list is required")
    private List<ProductDto> productsDto;

    public InvoiceDto()
    {
        productsDto = new ArrayList<>();
    }

    public InvoiceDto(LocalDateTime dateIssued, LocalDateTime dateDue, CompanyDto companyDto, List<ProductDto> productsDto)
    {
        this.dateIssued = dateIssued;
        this.dateDue = dateDue;
        this.companyDto = companyDto;
        this.productsDto = productsDto;
    }

    public void addProductItem(ProductDto product)
    {
        this.productsDto.add(product);
    }

    public Long getInvoiceId()
    {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId)
    {
        this.invoiceId = invoiceId;
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

    public List<ProductDto> getProductsDto()
    {
        return productsDto;
    }

    public void setProductsDto(List<ProductDto> productsDto)
    {
        this.productsDto = productsDto;
    }
}

package com.estafet.companies.utils;

import com.estafet.companies.configuration.ModelMapperConfiguration;
import com.estafet.companies.dto.CompanyDto;
import com.estafet.companies.dto.InvoiceDto;
import com.estafet.companies.dto.ProductDto;
import com.estafet.companies.model.Company;
import com.estafet.companies.model.Invoice;
import com.estafet.companies.model.Product;
import com.estafet.companies.model.ProductItem;
import com.estafet.companies.model.es.EsCompany;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@Import(ModelMapperConfiguration.class)
public class ModelMapperUtils
{
    @Autowired
    private ModelMapper modelMapper;

    public List<InvoiceDto> convertInvoiceListToDto(List<Invoice> invoiceList)
    {
        return invoiceList.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<Invoice> convertInvoiceDtoListToEntity(List<InvoiceDto> invoiceDtoList)
    {
        return invoiceDtoList.stream().map(this::convertToEntity).collect(Collectors.toList());
    }

    public List<CompanyDto> convertCompanyListToDto(List<Company> companyList)
    {
        return companyList.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<Company> convertCompanyDtoListToEntity(List<CompanyDto> companyDtoList)
    {
        return companyDtoList.stream().map(this::convertToEntity).collect(Collectors.toList());
    }

    public CompanyDto convertToDto(Company company)
    {
        return new CompanyDto(company.getTaxNumber(), company.getName(), company.getAddress(), company.getRepresentative());
    }

    public ProductDto convertToDto(ProductItem productItem)
    {
        return new ProductDto(productItem.getProduct().getName(), productItem.getProduct().getPrice(), productItem.getAmount());
    }

    public ProductItem convertToEntity(ProductDto productDto)
    {
        Product product = new Product(productDto.getName(), productDto.getPrice());
        ProductItem productItem = new ProductItem();
        productItem.setAmount(productDto.getProductAmount());
        productItem.setProduct(product);
        return productItem;
    }

    public Company convertToEntity(CompanyDto companyDto)
    {
        return new Company(companyDto.getTaxNumber(), companyDto.getName(), companyDto.getAddress(), companyDto.getRepresentative());
    }

    public InvoiceDto convertToDto(Invoice invoice)
    {
        InvoiceDto invDto = new InvoiceDto();
        invDto.setCompanyDto(convertToDto(invoice.getCompany()));
        invoice.getProductItems().forEach(item -> invDto.addProductItem(convertToDto(item)));
        invDto.setInvoiceId(invoice.getId());
        invDto.setDateIssued(invoice.getDateIssued());
        invDto.setDateDue(invoice.getDateDue());
        return invDto;
    }

    public Invoice convertToEntity(InvoiceDto invoiceDto)
    {
        Invoice invoice = new Invoice();
        invoice.setCompany(convertToEntity(invoiceDto.getCompanyDto()));
        invoice.setDateDue(invoiceDto.getDateDue());
        invoice.setDateIssued(invoiceDto.getDateIssued());

        List<ProductItem> products = new ArrayList<>();
        invoiceDto.getProductsDto().forEach(productDto -> products.add(convertToEntity(productDto)));
        invoice.setProductItems(products);

        return invoice;
    }

    /**
     * @param companyEs ElasticSearch document
     * @return Company DTO
     */
    public CompanyDto convertToDto(EsCompany companyEs)
    {
        return new CompanyDto(companyEs.getTaxNumber(), companyEs.getName(), companyEs.getAddress(), companyEs.getRepresentative());
    }

    /**
     * @param companyDto
     * @return ElasticSearch document
     */
    public EsCompany convertToEsEntity(CompanyDto companyDto)
    {
        return new EsCompany(companyDto.getTaxNumber(), companyDto.getName(), companyDto.getAddress(), companyDto.getRepresentative());
    }

    /**
     * @param esCompanyIterable List of elastic search company models
     * @return List of Company DTOs
     */
    public List<CompanyDto> convertCompanyListToDto(Iterable<EsCompany> esCompanyIterable)
    {
        return StreamSupport.stream(esCompanyIterable.spliterator(), false).map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * @param companyDtoList
     * @return List of ElasticSearch documents
     */
    public List<EsCompany> convertEsCompanyDtoListToEntity(List<CompanyDto> companyDtoList)
    {
        return companyDtoList.stream().map(this::convertToEsEntity).collect(Collectors.toList());
    }

    /**
     * Setter in case we are using the mapper out of Spring context
     *
     * @param modelMapper preferably instantiated from the preconfigured {@link com.estafet.companies.configuration.ModelMapperConfiguration}
     */
    public void setModelMapper(ModelMapper modelMapper)
    {
        this.modelMapper = modelMapper;
    }
}

package com.estafet.companies.utils;

import com.estafet.companies.configuration.ModelMapperConfiguration;
import com.estafet.companies.dto.CompanyDto;
import com.estafet.companies.dto.InvoiceDto;
import com.estafet.companies.dto.ProductDto;
import com.estafet.companies.model.Company;
import com.estafet.companies.model.Invoice;
import com.estafet.companies.model.Product;
import com.estafet.companies.model.ProductItem;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Import(ModelMapperConfiguration.class)
public class ModelMapperUtils
{
    @Autowired
    private ModelMapper modelMapper;

    public <S, T> List<T> mapList(List<S> source, Class<T> targetClass)
    {
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
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
     * Setter in case we are using the mapper out of Spring context
     *
     * @param modelMapper preferably instantiated from the preconfigured {@link com.estafet.companies.configuration.ModelMapperConfiguration}
     */
    public void setModelMapper(ModelMapper modelMapper)
    {
        this.modelMapper = modelMapper;
    }
}

package com.estafet.companies.configuration;

import com.estafet.companies.dto.InvoiceDto;
import com.estafet.companies.model.Invoice;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;

public class ModelMapperConfiguration
{
    @Bean
    public ModelMapper modelMapper()
    {
        ModelMapper m = new ModelMapper();

        m.typeMap(Invoice.class, InvoiceDto.class).addMappings(mapper ->
        {
            mapper.map(Invoice::getCompany, InvoiceDto::setCompanyDto);
            mapper.map(Invoice::getProducts, InvoiceDto::setProductsDto);
        });

        m.typeMap(InvoiceDto.class, Invoice.class).addMappings(mapper ->
        {
            mapper.map(InvoiceDto::getCompanyDto, Invoice::setCompany);
            mapper.map(InvoiceDto::getProductsDto, Invoice::setProducts);
        });

        return m;
    }
}

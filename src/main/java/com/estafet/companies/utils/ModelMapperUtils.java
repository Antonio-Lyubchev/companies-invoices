package com.estafet.companies.utils;

import com.estafet.companies.configuration.ModelMapperConfiguration;
import com.estafet.companies.dto.CompanyDto;
import com.estafet.companies.dto.InvoiceDto;
import com.estafet.companies.model.Company;
import com.estafet.companies.model.Invoice;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

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
        return modelMapper.map(company, CompanyDto.class);
    }

    public Company convertToEntity(CompanyDto companyDto)
    {
        return modelMapper.map(companyDto, Company.class);
    }

    public InvoiceDto convertToDto(Invoice invoice)
    {
        return modelMapper.map(invoice, InvoiceDto.class);
    }

    public Invoice convertToEntity(InvoiceDto invoiceDto)
    {
        return modelMapper.map(invoiceDto, Invoice.class);
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

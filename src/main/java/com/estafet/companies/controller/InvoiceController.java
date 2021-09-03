package com.estafet.companies.controller;

import com.estafet.companies.dto.CompanyDto;
import com.estafet.companies.dto.InvoiceDto;
import com.estafet.companies.exception.EntityNotFoundException;
import com.estafet.companies.exception.InvalidInputException;
import com.estafet.companies.model.Company;
import com.estafet.companies.model.Invoice;
import com.estafet.companies.service.CompanyService;
import com.estafet.companies.service.InvoiceService;
import com.estafet.companies.utils.JSONParser;
import com.estafet.companies.utils.ModelMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class InvoiceController
{
    private final InvoiceService invoiceService;
    private final CompanyService companyService;
    private final JSONParser parser;
    private final ModelMapperUtils modelMapperUtils;

    @Autowired
    InvoiceController(InvoiceService invoiceService, CompanyService companyService, JSONParser parser, ModelMapperUtils modelMapperUtils)
    {
        this.invoiceService = invoiceService;
        this.companyService = companyService;
        this.parser = parser;
        this.modelMapperUtils = modelMapperUtils;
    }

    @GetMapping(value = "/invoices", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<InvoiceDto> getAllInvoices()
    {
        List<Invoice> invoiceList = invoiceService.getAllInvoicesAsList();
        return invoiceList.stream().map(modelMapperUtils::convertToDto).collect(Collectors.toList());
    }

    @GetMapping(value = "/invoices/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public InvoiceDto getInvoiceById(@PathVariable("id") long invoiceId) throws EntityNotFoundException
    {
        Invoice invoice = invoiceService.getInvoice(invoiceId);
        return modelMapperUtils.convertToDto(invoice);
    }

    @PutMapping(value = "/invoices", produces = MediaType.APPLICATION_JSON_VALUE)
    public long addInvoice(@Valid @RequestBody InvoiceDto invoiceDto) throws InvalidInputException
    {
        Invoice invoice = modelMapperUtils.convertToEntity(invoiceDto);
        registerCustomer(invoiceDto);
        return invoiceService.addInvoice(invoice);
    }

    @PostMapping(value = "/invoices/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateInvoice(@PathVariable("id") long invoiceId, @Valid @RequestBody InvoiceDto invoiceDto) throws InvalidInputException
    {
        invoiceService.updateInvoice(invoiceId, modelMapperUtils.convertToEntity(invoiceDto));
    }

    @DeleteMapping(value = "/invoices/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteInvoice(@PathVariable("id") long invoiceId) throws EntityNotFoundException
    {
        invoiceService.deleteInvoice(invoiceId);
    }

    @PostMapping(path = "/invoices", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void addInvoice(@RequestPart MultipartFile file) throws IOException, InvalidInputException
    {
        List<InvoiceDto> invoiceDtoList = parser.fromJsonToList(file.getBytes(), InvoiceDto.class);
        List<Invoice> invoiceList = modelMapperUtils.mapList(invoiceDtoList, Invoice.class);
        invoiceService.addInvoices(invoiceList);
    }

    private void registerCustomer(InvoiceDto invoiceDto) throws InvalidInputException
    {
        CompanyDto companyDto = invoiceDto.getCompanyDto();
        if (isFullyQualifiedCompanyDto(companyDto))
        {
            Company companyToRegister = modelMapperUtils.convertToEntity(invoiceDto.getCompanyDto());
            companyService.addCompany(companyToRegister);
            return;
        }

        Company companyToRegister = modelMapperUtils.convertToEntity(invoiceDto.getCompanyDto());

        try
        {
            // ignore result, we just want to know if this company is present in storage
            companyService.getCompany(companyToRegister.getTaxNumber());
        } catch (EntityNotFoundException e)
        {
            throw new InvalidInputException("Cannot generate an invoice for a company that does not exist");
        }
    }

    private boolean isFullyQualifiedCompanyDto(CompanyDto companyDto)
    {
        return companyDto != null
                && companyDto.getTaxNumber() != null
                && StringUtils.hasText(companyDto.getName())
                && StringUtils.hasText(companyDto.getAddress())
                && StringUtils.hasText(companyDto.getRepresentative());
    }
}

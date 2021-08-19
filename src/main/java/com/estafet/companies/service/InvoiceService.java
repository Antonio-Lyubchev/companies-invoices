package com.estafet.companies.service;

import com.estafet.companies.exception.ApiException;
import com.estafet.companies.exception.EntityNotFoundException;
import com.estafet.companies.exception.InvalidInputException;
import com.estafet.companies.model.Invoice;
import com.estafet.companies.model.InvoiceProduct;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class InvoiceService
{
    private final CompanyService companyService;
    private final HashMap<String, Invoice> invoiceMap = new HashMap<>();

    public InvoiceService(CompanyService companyService)
    {
        this.companyService = companyService;

        List<InvoiceProduct> sampleProducts = new ArrayList<>();
        sampleProducts.add(new InvoiceProduct("product1", new BigDecimal("3.14"), 10));
        sampleProducts.add(new InvoiceProduct("product2", new BigDecimal("15.32"), 2));
        sampleProducts.add(new InvoiceProduct("product3", new BigDecimal("150"), 6));

        invoiceMap.put("invoiceNumber1", new Invoice(LocalDateTime.now(), LocalDateTime.now().plusDays(30), "invoiceNumber1", "tax1", sampleProducts));
        invoiceMap.put("invoiceNumber2", new Invoice(LocalDateTime.now(), LocalDateTime.now().plusDays(30), "invoiceNumber2", "tax2", sampleProducts));
        invoiceMap.put("invoiceNumber3", new Invoice(LocalDateTime.now(), LocalDateTime.now().plusDays(30), "invoiceNumber3", "tax3", sampleProducts));
    }

    public HashMap<String, Invoice> getInvoiceMap()
    {
        return invoiceMap;
    }

    public Invoice getInvoice(String invoiceNumber) throws EntityNotFoundException, InvalidInputException
    {
        if (!StringUtils.hasText(invoiceNumber))
        {
            throw new InvalidInputException("Invoice number is invalid!");
        }

        Invoice resultInvoice = invoiceMap.getOrDefault(invoiceNumber, null);
        if (resultInvoice == null)
        {
            throw new EntityNotFoundException("Invoice '" + invoiceNumber + "' not found!");
        }

        return resultInvoice;
    }

    // TODO: should there be an error if we are trying to overwrite an existing entry?
    public void addInvoice(Invoice invoice) throws InvalidInputException
    {
        if (invoice == null)
        {
            throw new InvalidInputException("Invoice is invalid!");
        }

        // TODO: change company key from name to tax number
        if (companyService.getCompanyMap().values().stream().noneMatch(c -> c.getTaxNumber().equals(invoice.getCompanyTaxNumber())))
        {
            throw new InvalidInputException("Company with tax number '" + invoice.getCompanyTaxNumber() + "' was not found!");
        }

        invoiceMap.put(invoice.getInvoiceNumber(), invoice);
    }

    public void updateInvoice(Invoice updatedInvoice) throws InvalidInputException
    {
        if (updatedInvoice == null)
        {
            throw new InvalidInputException("Invoice is invalid!");
        }

        if (!StringUtils.hasText(updatedInvoice.getInvoiceNumber()))
        {
            throw new InvalidInputException("Invoice number is invalid!");
        }

        invoiceMap.put(updatedInvoice.getInvoiceNumber(), updatedInvoice);
    }

    public void deleteInvoice(String invoiceNumber) throws ApiException
    {
        if (!StringUtils.hasText(invoiceNumber))
        {
            throw new InvalidInputException("Invoice number is invalid!");
        }

        if (invoiceMap.remove(invoiceNumber) == null)
        {
            throw new ApiException("Tried to remove a non-existing invoice");
        }
    }
}

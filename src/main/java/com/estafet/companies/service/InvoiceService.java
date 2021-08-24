package com.estafet.companies.service;

import com.estafet.companies.exception.ApiException;
import com.estafet.companies.exception.EntityNotFoundException;
import com.estafet.companies.exception.InvalidInputException;
import com.estafet.companies.model.Invoice;
import com.estafet.companies.model.InvoiceProduct;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Service
public class InvoiceService
{
    private final CompanyService companyService;
    private final List<Invoice> invoiceList;

    private AtomicInteger invoiceCounter;

    public InvoiceService(CompanyService companyService)
    {
        this.companyService = companyService;

        List<InvoiceProduct> sampleProducts = new ArrayList<>();
        sampleProducts.add(new InvoiceProduct("product1", new BigDecimal("3.14"), 10));
        sampleProducts.add(new InvoiceProduct("product2", new BigDecimal("15.32"), 2));
        sampleProducts.add(new InvoiceProduct("product3", new BigDecimal("150"), 6));

        invoiceList = new ArrayList<>(Arrays.asList(
                new Invoice(LocalDateTime.now(), LocalDateTime.now().plusDays(30), "tax1", sampleProducts),
                new Invoice(LocalDateTime.now(), LocalDateTime.now().plusDays(30), "tax2", sampleProducts),
                new Invoice(LocalDateTime.now(), LocalDateTime.now().plusDays(30), "tax3", sampleProducts)
        ));
    }

    public Invoice getInvoice(int invoiceNumber) throws EntityNotFoundException, InvalidInputException
    {
        return invoiceList.stream()
                .filter(c -> c.getInvoiceId() == invoiceNumber)
                .findAny()
                .orElseThrow(() -> new EntityNotFoundException("Invoice '" + invoiceNumber + "' not found!"));
    }

    public int addInvoice(Invoice newInvoice) throws InvalidInputException
    {
        if (newInvoice == null)
        {
            throw new InvalidInputException("Invoice is invalid!");
        }

        if (!companyService.isCompanyExistent(newInvoice.getCompanyTaxId()))
        {
            throw new InvalidInputException("Company with tax number '" + newInvoice.getCompanyTaxId() + "' was not found!");
        }

        newInvoice.setInvoiceId(getNextUniqueIndex());
        invoiceList.add(newInvoice);

        return newInvoice.getInvoiceId();
    }

    public void addInvoices(List<Invoice> invoices) throws InvalidInputException
    {
        for (Invoice invoice : invoices)
        {
            addInvoice(invoice);
        }
    }

    public void updateInvoice(int invoiceId, Invoice updatedInvoice) throws InvalidInputException
    {
        if (updatedInvoice == null)
        {
            throw new InvalidInputException("Invoice is invalid!");
        }

        int indexToUpdate = IntStream.range(0, invoiceList.size())
                .filter(i -> invoiceList.get(i).getInvoiceId() == invoiceId)
                .findFirst()
                .orElse(-1);

        if (indexToUpdate != -1)
        {
            invoiceList.set(indexToUpdate, updatedInvoice);
        } else
        {
            invoiceList.add(updatedInvoice);
        }
    }

    public void deleteInvoice(int invoiceId) throws ApiException
    {
        // if no elements got removed, throw so we can return 404
        if (!invoiceList.removeIf(i -> i.getInvoiceId() == invoiceId))
        {
            throw new EntityNotFoundException("Tried to remove a non-existing invoice");
        }
    }

    public List<Invoice> getAllInvoicesAsList()
    {
        return Collections.unmodifiableList(invoiceList);
    }

    private int getNextUniqueIndex()
    {
        return invoiceCounter.getAndIncrement();
    }
}

package com.estafet.companies.service;

import com.estafet.companies.exception.EntityNotFoundException;
import com.estafet.companies.exception.InvalidInputException;
import com.estafet.companies.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Service
@Deprecated
public class InMemoryInvoiceServiceImpl implements InvoiceService
{
    private final InMemoryCompanyServiceImpl companyService;
    private final List<Invoice> invoiceList;

    private final AtomicInteger invoiceCounter;

    @Autowired
    public InMemoryInvoiceServiceImpl(InMemoryCompanyServiceImpl companyService)
    {
        this.companyService = companyService;

        invoiceList = new ArrayList<>();
        invoiceCounter = new AtomicInteger();
    }

    @Override
    public Invoice getInvoice(long invoiceNumber)
    {
        return invoiceList.stream()
                .filter(c -> c.getInvoiceId() == invoiceNumber)
                .findAny()
                .orElseThrow(() -> new EntityNotFoundException("Invoice '" + invoiceNumber + "' not found!"));
    }

    @Override
    public long addInvoice(Invoice newInvoice)
    {
        if (newInvoice == null)
        {
            throw new InvalidInputException("Invoice is invalid!");
        }

        if (!companyService.isCompanyExistent(newInvoice.getCompany()))
        {
            throw new InvalidInputException("Company with tax number '" + newInvoice.getCompany().getTaxNumber() + "' was not found!");
        }

        newInvoice.setInvoiceId(getNextUniqueIndex());
        invoiceList.add(newInvoice);

        return newInvoice.getInvoiceId();
    }

    @Override
    public void addInvoices(List<Invoice> invoices)
    {
        for (Invoice invoice : invoices)
        {
            addInvoice(invoice);
        }
    }

    @Override
    public void updateInvoice(long invoiceId, Invoice updatedInvoice)
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

    @Override
    public void deleteInvoice(long invoiceId)
    {
        // if no elements got removed, throw so we can return 404
        if (!invoiceList.removeIf(i -> i.getInvoiceId() == invoiceId))
        {
            throw new EntityNotFoundException("Tried to remove a non-existing invoice");
        }
    }

    @Override
    public List<Invoice> getAllInvoicesAsList()
    {
        return Collections.unmodifiableList(invoiceList);
    }

    private int getNextUniqueIndex()
    {
        return invoiceCounter.getAndIncrement();
    }
}

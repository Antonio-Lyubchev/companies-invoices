package com.estafet.companies.service.impl.db;

import com.estafet.companies.exception.EntityNotFoundException;
import com.estafet.companies.exception.InvalidInputException;
import com.estafet.companies.model.Invoice;
import com.estafet.companies.repository.jpa.InvoiceRepository;
import com.estafet.companies.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Primary
@Service
public class DbInvoiceServiceImpl implements InvoiceService
{
    @Autowired
    private InvoiceRepository repository;

    @Override
    public Invoice getInvoice(long invoiceNumber) throws EntityNotFoundException
    {
        Optional<Invoice> invoice = repository.findById(invoiceNumber);
        if (invoice.isEmpty())
        {
            throw new EntityNotFoundException("Invoice with number '" + invoiceNumber + "' not found!");
        }

        return invoice.get();
    }

    @Override
    public long addInvoice(Invoice newInvoice)
    {
        return repository.save(newInvoice).getId();
    }

    @Override
    public void addInvoices(List<Invoice> invoices)
    {
        repository.saveAll(invoices);
    }

    @Override
    public void updateInvoice(long invoiceId, Invoice newInvoice) throws InvalidInputException
    {
        if (newInvoice == null)
        {
            throw new InvalidInputException("Invalid invoice!");
        }

        if (invoiceId != newInvoice.getId())
        {
            throw new InvalidInputException("You cannot change invoice ID of existing invoice!");
        }

        Optional<Invoice> oldInvoice = repository.findById(invoiceId);
        Invoice invoiceToUpdate;

        // copy fields
        invoiceToUpdate = oldInvoice.orElse(newInvoice);

        repository.save(invoiceToUpdate);
    }

    @Override
    public void deleteInvoice(long invoiceId) throws EntityNotFoundException
    {
        try
        {
            repository.deleteById(invoiceId);
        } catch (EmptyResultDataAccessException ex)
        {
            throw new EntityNotFoundException("Invoice with id '" + invoiceId + "' does not exist!");
        }
    }

    @Override
    public List<Invoice> getAllInvoicesAsList()
    {
        return repository.findAll();
    }
}

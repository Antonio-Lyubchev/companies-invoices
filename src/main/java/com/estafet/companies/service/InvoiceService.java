package com.estafet.companies.service;

import com.estafet.companies.exception.EntityNotFoundException;
import com.estafet.companies.exception.InvalidInputException;
import com.estafet.companies.model.Invoice;

import java.util.List;

public interface InvoiceService
{
    Invoice getInvoice(long invoiceNumber) throws EntityNotFoundException;

    long addInvoice(Invoice newInvoice) throws InvalidInputException;

    void addInvoices(List<Invoice> invoices) throws InvalidInputException;

    void updateInvoice(long invoiceId, Invoice updatedInvoice) throws InvalidInputException;

    void deleteInvoice(long invoiceId) throws EntityNotFoundException;

    List<Invoice> getAllInvoicesAsList();
}

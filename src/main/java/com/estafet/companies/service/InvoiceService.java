package com.estafet.companies.service;

import com.estafet.companies.model.Invoice;

import java.util.List;

public interface InvoiceService
{
    Invoice getInvoice(long invoiceNumber);

    long addInvoice(Invoice newInvoice);

    void addInvoices(List<Invoice> invoices);

    void updateInvoice(long invoiceId, Invoice updatedInvoice);

    void deleteInvoice(long invoiceId);

    List<Invoice> getAllInvoicesAsList();
}

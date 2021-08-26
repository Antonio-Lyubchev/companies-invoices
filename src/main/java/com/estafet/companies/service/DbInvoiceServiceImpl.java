package com.estafet.companies.service;

import com.estafet.companies.model.Invoice;
import com.estafet.companies.repository.CompanyRepository;
import com.estafet.companies.repository.InvoiceRepository;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service
public class DbInvoiceServiceImpl implements InvoiceService
{
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public Invoice getInvoice(long invoiceNumber)
    {
        throw new NotYetImplementedException();
    }

    @Override
    public long addInvoice(Invoice newInvoice)
    {
        return invoiceRepository.save(newInvoice).getInvoiceId();
    }

    @Override
    public void addInvoices(List<Invoice> invoices)
    {
        invoiceRepository.saveAll(invoices);
    }

    @Override
    public void updateInvoice(long invoiceId, Invoice updatedInvoice)
    {
        throw new NotYetImplementedException();
    }

    @Override
    public void deleteInvoice(long invoiceId)
    {
        throw new NotYetImplementedException();
    }

    @Override
    public List<Invoice> getAllInvoicesAsList()
    {
        return invoiceRepository.findAll();
    }
}

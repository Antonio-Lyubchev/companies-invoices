package com.estafet.companies.controller;

import com.estafet.companies.exception.ApiException;
import com.estafet.companies.exception.EntityNotFoundException;
import com.estafet.companies.exception.InvalidInputException;
import com.estafet.companies.model.Invoice;
import com.estafet.companies.service.InvoiceService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class InvoiceController
{
    private final InvoiceService invoiceService;

    InvoiceController(InvoiceService invoiceService)
    {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/invoices")
    public HashMap<String, Invoice> getAllInvoices()
    {
        return invoiceService.getInvoiceMap();
    }

    @GetMapping("/invoices/{id}")
    public Invoice getInvoiceById(@PathVariable("id") String invoiceId) throws EntityNotFoundException, InvalidInputException
    {
        return invoiceService.getInvoice(invoiceId);
    }

    @PostMapping("/invoices")
    public void addInvoice(@RequestBody Invoice invoice) throws InvalidInputException
    {
        invoiceService.addInvoice(invoice);
    }

    @PutMapping("/invoices/{id}")
    public void updateInvoice(@PathVariable("id") String invoiceId, @RequestBody Invoice invoice) throws InvalidInputException
    {
        invoiceService.updateInvoice(invoiceId, invoice);
    }

    @DeleteMapping("/invoices/{id}")
    public void deleteInvoice(@PathVariable("id") String invoiceId) throws ApiException
    {
        invoiceService.deleteInvoice(invoiceId);
    }
}

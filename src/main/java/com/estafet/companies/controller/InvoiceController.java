package com.estafet.companies.controller;

import com.estafet.companies.exception.ApiException;
import com.estafet.companies.exception.EntityNotFoundException;
import com.estafet.companies.exception.InvalidInputException;
import com.estafet.companies.model.Invoice;
import com.estafet.companies.model.InvoiceRequest;
import com.estafet.companies.service.CompanyService;
import com.estafet.companies.service.InvoiceService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class InvoiceController
{
    private final InvoiceService invoiceService;
    private final CompanyService companyService;

    InvoiceController(InvoiceService invoiceService, CompanyService companyService)
    {
        this.invoiceService = invoiceService;
        this.companyService = companyService;
    }

    @GetMapping("/invoices")
    public List<Invoice> getAllInvoices()
    {
        return new ArrayList<>(invoiceService.getAllInvoicesAsList());
    }

    @GetMapping("/invoices/{id}")
    public Invoice getInvoiceById(@PathVariable("id") String invoiceId) throws EntityNotFoundException, InvalidInputException
    {
        return invoiceService.getInvoice(invoiceId);
    }

    @PutMapping("/invoices")
    public int addInvoice(@RequestBody InvoiceRequest request) throws InvalidInputException
    {
        Invoice invoice = prepareInvoice(request);
        String companyId = registerCustomer(request);
        invoiceService.addInvoice(invoice);
        return invoiceService.getInvoiceCount();
    }

    private Invoice prepareInvoice(InvoiceRequest request)
    {
        return new Invoice(request.getDateIssued(),
                request.getDateDue(),
                String.valueOf(invoiceService.getInvoiceCount() + 1),
                request.getCompany().getTaxId(),
                request.getProducts());
    }


    private String registerCustomer(InvoiceRequest request) throws InvalidInputException
    {
        return companyService.addCompany(request.getCompany());
    }


    @PostMapping("/invoices/{id}")
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

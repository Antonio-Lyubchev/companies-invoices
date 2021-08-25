package com.estafet.companies.controller;

import com.estafet.companies.exception.ApiException;
import com.estafet.companies.exception.EntityNotFoundException;
import com.estafet.companies.exception.InvalidInputException;
import com.estafet.companies.model.Invoice;
import com.estafet.companies.model.InvoiceRequest;
import com.estafet.companies.service.CompanyService;
import com.estafet.companies.service.InvoiceService;
import com.estafet.companies.utils.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class InvoiceController
{
    private final InvoiceService invoiceService;
    private final CompanyService companyService;
    private final JSONParser parser;

    @Autowired
    InvoiceController(InvoiceService invoiceService, CompanyService companyService, JSONParser parser)
    {
        this.invoiceService = invoiceService;
        this.companyService = companyService;
        this.parser = parser;
    }

    @GetMapping("/invoices")
    public List<Invoice> getAllInvoices()
    {
        return invoiceService.getAllInvoicesAsList();
    }

    @GetMapping("/invoices/{id}")
    public Invoice getInvoiceById(@PathVariable("id") int invoiceId) throws EntityNotFoundException, InvalidInputException
    {
        return invoiceService.getInvoice(invoiceId);
    }

    @PutMapping("/invoices")
    public int addInvoice(@RequestBody InvoiceRequest request) throws InvalidInputException
    {
        Invoice invoice = prepareInvoice(request);
        String companyId = registerCustomer(request);
        return invoiceService.addInvoice(invoice);
    }

    private Invoice prepareInvoice(InvoiceRequest request)
    {
        return new Invoice(request.getDateIssued(),
                request.getDateDue(),
                request.getCompany().getTaxId(),
                request.getProducts());
    }


    private String registerCustomer(InvoiceRequest request) throws InvalidInputException
    {
        return companyService.addCompany(request.getCompany());
    }

    @PostMapping("/invoices/{id}")
    public void updateInvoice(@PathVariable("id") int invoiceId, @RequestBody Invoice invoice) throws InvalidInputException
    {
        invoiceService.updateInvoice(invoiceId, invoice);
    }

    @DeleteMapping("/invoices/{id}")
    public void deleteInvoice(@PathVariable("id") int invoiceId) throws ApiException
    {
        invoiceService.deleteInvoice(invoiceId);
    }

    @PostMapping(path = "/invoices", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void addInvoice(@RequestPart MultipartFile file) throws IOException, InvalidInputException
    {
        List<Invoice> invoices = parser.fromJsonToList(file.getBytes(), Invoice.class);

        invoiceService.addInvoices(invoices);
    }
}

package com.estafet.companies.model;

import java.time.LocalDateTime;
import java.util.List;

public class Invoice
{
    private LocalDateTime dateIssued;
    private LocalDateTime dateDue;
    private String invoiceNumber; // TODO: Can it be numerical?
    private String companyTaxNumber;
    private List<InvoiceProduct> products;

    public Invoice()
    {
    }

    public Invoice(LocalDateTime dateIssued, LocalDateTime dateDue, String invoiceNumber, String companyTaxNumber, List<InvoiceProduct> products)
    {
        this.dateIssued = dateIssued;
        this.dateDue = dateDue;
        this.invoiceNumber = invoiceNumber;
        this.companyTaxNumber = companyTaxNumber;
        this.products = products;
    }

    public LocalDateTime getDateIssued()
    {
        return dateIssued;
    }

    public void setDateIssued(LocalDateTime dateIssued)
    {
        this.dateIssued = dateIssued;
    }

    public LocalDateTime getDateDue()
    {
        return dateDue;
    }

    public void setDateDue(LocalDateTime dateDue)
    {
        this.dateDue = dateDue;
    }

    public String getInvoiceNumber()
    {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber)
    {
        this.invoiceNumber = invoiceNumber;
    }

    public String getCompanyTaxNumber()
    {
        return companyTaxNumber;
    }

    public void setCompanyTaxNumber(String companyTaxNumber)
    {
        this.companyTaxNumber = companyTaxNumber;
    }

    public List<InvoiceProduct> getProducts()
    {
        return products;
    }

    public void setProducts(List<InvoiceProduct> products)
    {
        this.products = products;
    }
}

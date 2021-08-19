package com.estafet.companies.model;

import java.time.LocalDateTime;
import java.util.List;

public class Invoice
{
    private LocalDateTime dateIssued;
    private LocalDateTime dateDue;
    private String invoiceId;
    private String companyTaxId;
    private List<InvoiceProduct> products;

    public Invoice()
    {
    }

    public Invoice(LocalDateTime dateIssued, LocalDateTime dateDue, String invoiceId, String companyTaxId, List<InvoiceProduct> products)
    {
        this.dateIssued = dateIssued;
        this.dateDue = dateDue;
        this.invoiceId = invoiceId;
        this.companyTaxId = companyTaxId;
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

    public String getInvoiceId()
    {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId)
    {
        this.invoiceId = invoiceId;
    }

    public String getCompanyTaxId()
    {
        return companyTaxId;
    }

    public void setCompanyTaxId(String companyTaxId)
    {
        this.companyTaxId = companyTaxId;
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

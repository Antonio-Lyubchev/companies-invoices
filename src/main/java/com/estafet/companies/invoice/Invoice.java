package com.estafet.companies.invoice;

import java.util.Date;
import java.util.List;

public class Invoice
{
    private Date dateIssued; // TODO: Is this the only date needed?
    private String invoiceNumber; // TODO: Can it be numerical?
    private String companyTaxNumber;
    private List<InvoiceProduct> products;

    public Invoice()
    {
    }

    public Invoice(Date dateIssued, String invoiceNumber, String companyTaxNumber, List<InvoiceProduct> products)
    {
        this.dateIssued = dateIssued;
        this.invoiceNumber = invoiceNumber;
        this.companyTaxNumber = companyTaxNumber;
        this.products = products;
    }

    public Date getDateIssued()
    {
        return dateIssued;
    }

    public void setDateIssued(Date dateIssued)
    {
        this.dateIssued = dateIssued;
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

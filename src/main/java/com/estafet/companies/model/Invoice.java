package com.estafet.companies.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class Invoice
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private LocalDateTime dateIssued;
    @NotNull
    private LocalDateTime dateDue;
    @NotNull
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "company_id")
    private Company company;
    @NotEmpty
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductItem> productItems;

    public Invoice()
    {
    }

    public Invoice(LocalDateTime dateIssued, LocalDateTime dateDue, Company company, List<ProductItem> productItems)
    {
        this.dateIssued = dateIssued;
        this.dateDue = dateDue;
        this.company = company;
        this.productItems = productItems;
    }

    public Invoice(Invoice other)
    {
        this.id = other.id;
        this.dateIssued = other.dateIssued;
        this.dateDue = other.dateDue;
        this.company = other.company;
        this.productItems = other.productItems;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
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

    public Company getCompany()
    {
        return company;
    }

    public void setCompany(Company company)
    {
        this.company = company;
    }

    public List<ProductItem> getProductItems()
    {
        return productItems;
    }

    public void setProductItems(List<ProductItem> productItems)
    {
        this.productItems = productItems;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invoice invoice = (Invoice) o;
        return id.equals(invoice.id) && dateIssued.equals(invoice.dateIssued) && dateDue.equals(invoice.dateDue) && company.equals(invoice.company) && productItems.equals(invoice.productItems);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, dateIssued, dateDue, company, productItems);
    }
}

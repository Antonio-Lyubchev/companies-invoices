package com.estafet.companies.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

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
    @NotNull
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Product> products;

    public Invoice()
    {
    }

    public Invoice(LocalDateTime dateIssued, LocalDateTime dateDue, Company company, List<Product> products)
    {
        this.dateIssued = dateIssued;
        this.dateDue = dateDue;
        this.company = company;
        this.products = products;
    }

    public Invoice(Invoice other)
    {
        this.id = other.id;
        this.dateIssued = other.dateIssued;
        this.dateDue = other.dateDue;
        this.company = other.company;
        this.products = other.products;
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

    public List<Product> getProducts()
    {
        return products;
    }

    public void setProducts(List<Product> products)
    {
        this.products = products;
    }
}

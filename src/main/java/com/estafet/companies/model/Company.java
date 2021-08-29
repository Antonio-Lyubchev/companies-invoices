package com.estafet.companies.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Company
{
    @Id
    @Column(name = "taxId")
    @NotEmpty
    private String taxNumber;
    @NotEmpty
    private String name;
    @NotEmpty
    private String address;
    @NotEmpty
    private String representative;

    public Company()
    {
    }

    public Company(String taxNumber, String name, String address, String representative)
    {
        this.taxNumber = taxNumber;
        this.name = name;
        this.address = address;
        this.representative = representative;
    }

    public Company(Company other)
    {
        this.taxNumber = other.taxNumber;
        this.name = other.name;
        this.address = other.address;
        this.representative = other.representative;
    }

    public String getTaxNumber()
    {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber)
    {
        this.taxNumber = taxNumber;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getRepresentative()
    {
        return representative;
    }

    public void setRepresentative(String representative)
    {
        this.representative = representative;
    }
}

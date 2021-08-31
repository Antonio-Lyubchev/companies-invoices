package com.estafet.companies.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class Company
{
    @Id
    @NotNull
    @Column(name = "taxId")
    private long taxNumber;
    @NotEmpty
    private String name;
    @NotEmpty
    private String address;
    @NotEmpty
    private String representative;

    public Company()
    {
    }

    public Company(long taxNumber, String name, String address, String representative)
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

    public Long getTaxNumber()
    {
        return taxNumber;
    }

    public void setTaxNumber(long taxNumber)
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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return taxNumber == company.taxNumber && name.equals(company.name) && address.equals(company.address) && representative.equals(company.representative);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(taxNumber, name, address, representative);
    }
}

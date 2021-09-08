package com.estafet.companies.model.es;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Objects;

/**
 * Company model for elastic search persistence
 */
@Document(indexName = "companyindex")
public class EsCompany
{
    @Id
    private Long taxNumber;
    private String name;
    private String address;
    private String representative;

    public EsCompany()
    {
    }

    public EsCompany(Long taxNumber, String name, String address, String representative)
    {
        this.taxNumber = taxNumber;
        this.name = name;
        this.address = address;
        this.representative = representative;
    }

    public EsCompany(EsCompany other)
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
        EsCompany company = (EsCompany) o;
        return taxNumber.equals(company.taxNumber) && name.equals(company.name) && address.equals(company.address) && representative.equals(company.representative);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(taxNumber, name, address, representative);
    }
}

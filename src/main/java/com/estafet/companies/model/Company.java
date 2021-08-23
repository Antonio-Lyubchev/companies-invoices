package com.estafet.companies.model;

public class Company
{
    private String name;
    private String taxId;
    private String address;
    private String representative;

    public Company()
    {
    }

    public Company(String name, String taxId, String address, String representative)
    {
        this.name = name;
        this.taxId = taxId;
        this.address = address;
        this.representative = representative;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getTaxId()
    {
        return taxId;
    }

    public void setTaxId(String taxId)
    {
        this.taxId = taxId;
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

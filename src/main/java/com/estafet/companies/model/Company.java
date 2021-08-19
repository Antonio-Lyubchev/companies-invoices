package com.estafet.companies.model;

public class Company
{
    private String name;
    private String taxNumber;
    private String address;
    private String representative;

    public Company()
    {
    }

    public Company(String name, String taxNumber, String address, String representative)
    {
        this.name = name;
        this.taxNumber = taxNumber;
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

    public String getTaxNumber()
    {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber)
    {
        this.taxNumber = taxNumber;
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
    public String toString()
    {
        return "Company{" +
                "name='" + name + '\'' +
                ", taxNumber='" + taxNumber + '\'' +
                ", address='" + address + '\'' +
                ", representative='" + representative + '\'' +
                '}';
    }
}

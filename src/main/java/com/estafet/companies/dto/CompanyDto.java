package com.estafet.companies.dto;

public class CompanyDto
{
    private String taxNumber;
    private String name;
    private String address;
    private String representative;

    public CompanyDto()
    {
    }

    public CompanyDto(String taxNumber, String name, String address, String representative)
    {
        this.taxNumber = taxNumber;
        this.name = name;
        this.address = address;
        this.representative = representative;
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

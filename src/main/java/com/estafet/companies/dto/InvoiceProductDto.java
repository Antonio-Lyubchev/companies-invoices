package com.estafet.companies.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.math.RoundingMode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceProductDto
{
    private String name;
    private BigDecimal price;
    private int productAmount;

    public InvoiceProductDto()
    {
    }

    public InvoiceProductDto(String name, BigDecimal price, int productAmount)
    {
        this.name = name;
        this.price = price;
        this.productAmount = productAmount;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public int getProductAmount()
    {
        return productAmount;
    }

    public void setProductAmount(int productAmount)
    {
        this.productAmount = productAmount;
    }

    public BigDecimal getTotalProductPrice()
    {
        return price.multiply(BigDecimal.valueOf(productAmount)).setScale(2, RoundingMode.HALF_EVEN);
    }
}

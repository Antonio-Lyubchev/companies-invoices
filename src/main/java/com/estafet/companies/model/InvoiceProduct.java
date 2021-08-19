package com.estafet.companies.model;

import java.math.BigDecimal;

/**
 * This class represents a single invoice product, including the product quantity.
 */
public class InvoiceProduct
{
    private String name;
    private BigDecimal price;
    private int productAmount; // TODO: Is int the best type?

    public InvoiceProduct()
    {
    }

    public InvoiceProduct(String name, BigDecimal price, int amount)
    {
        this.name = name;
        this.price = price;
        this.productAmount = amount;
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
        return price.multiply(BigDecimal.valueOf(productAmount));
    }
}

package com.estafet.companies.invoice;

/**
 * This class represents a single invoice product, including the product quantity.
 */
public class InvoiceProduct
{
    private String name;
    private double price;
    private int productAmount; // TODO: Is int the best type?

    public InvoiceProduct()
    {
    }

    public InvoiceProduct(String name, double price, int amount)
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

    public double getPrice()
    {
        return price;
    }

    public void setPrice(double price)
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

    public double getTotalProductPrice()
    {
        return price * productAmount;
    }
}

package com.estafet.companies.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * This class represents a single invoice product, including the product quantity.
 */
// TODO: 3 part composite key? We will fill up the database if we keep inserting products that are the same for every new invoice
@Entity
public class Product
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    private String name;
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Product price must be greater than {value}")
    private BigDecimal price;
    @NotNull
    @Min(value = 0, message = "Product amount must be greater than {value}")
    private Long productAmount;

    public Product()
    {
    }

    public Product(String name, BigDecimal price, Long amount)
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

    public Long getProductAmount()
    {
        return productAmount;
    }

    public void setProductAmount(Long productAmount)
    {
        this.productAmount = productAmount;
    }
}

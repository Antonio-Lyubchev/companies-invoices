package com.estafet.companies.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ProductDto
{
    @NotEmpty(message = "Product name is required")
    private String name;
    @NotNull(message = "Product price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Product price must be greater than {value}")
    private BigDecimal price;
    @NotNull(message = "Product quantity is required")
    @Min(value = 0, message = "Product amount must be greater than {value}")
    private double productAmount;

    public ProductDto()
    {
    }

    public ProductDto(String name, BigDecimal price, double productAmount)
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

    public double getProductAmount()
    {
        return productAmount;
    }

    public void setProductAmount(double productAmount)
    {
        this.productAmount = productAmount;
    }

    public BigDecimal getTotalProductPrice()
    {
        return price.multiply(BigDecimal.valueOf(productAmount)).setScale(2, RoundingMode.HALF_EVEN);
    }
}

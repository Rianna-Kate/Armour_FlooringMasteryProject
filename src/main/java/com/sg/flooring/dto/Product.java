package com.sg.flooring.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private String productType;
    private BigDecimal costPerSqft;
    private BigDecimal laborCostPerSqft;

    public Product(String productType, String costPerSqft, String laborCostPerSqft) {
        this.productType = productType;
        this.costPerSqft = new BigDecimal(costPerSqft);
        this.laborCostPerSqft = new BigDecimal(laborCostPerSqft);
    }


    public String getProductType() {
        return productType;
    }

    public BigDecimal getCostPerSqft() {
        return costPerSqft;
    }

    public BigDecimal getLaborCostPerSqft() {
        return laborCostPerSqft;
    }

    @Override
    public String toString() {
        return productType;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(productType, product.productType) && Objects.equals(costPerSqft, product.costPerSqft) && Objects.equals(laborCostPerSqft, product.laborCostPerSqft);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productType, costPerSqft, laborCostPerSqft);
    }
}

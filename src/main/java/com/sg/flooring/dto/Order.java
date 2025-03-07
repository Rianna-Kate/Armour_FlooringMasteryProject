package com.sg.flooring.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class Order {
    private int orderNumber; // Setting to 0 for generation purposes
    private LocalDate orderDate;
    private String customerName;
    private BigDecimal area;
    private StateTax stateTax;
    private Product product;

    public Order () {
        orderNumber = 0;
        orderDate = null;
        customerName = null;
        area = null;
        stateTax = null;
        product = null;
    }
    public Order(String customerName) {
        this.customerName = customerName; // Incrementing upon new generation of order.
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public StateTax getStateTax() {
        return stateTax;
    }

    public void setStateTax(StateTax stateTax) {
        this.stateTax = stateTax;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    // Calculated Methods
    public BigDecimal getMaterialCost() {
        return area.multiply(getProduct().getCostPerSqft());
    }

    public BigDecimal getLaborCost() {
        return area.multiply(getProduct().getLaborCostPerSqft());
    }

    public BigDecimal getTax() {
        BigDecimal result = getMaterialCost().add(getLaborCost());
        result = result.multiply(getStateTax().getTaxRate().divide(new BigDecimal(100)));
        result = result.setScale(2, RoundingMode.HALF_UP);
        return result;
    }

    public BigDecimal getTotal() {
        return getMaterialCost().add(getLaborCost()).add(getTax());
    }

    public String toString() {
        return String.format("%s,%s," +
                        "%s,%s,%s,%s,%s,%s,%s,%s," +
                        "%s,%s",
                getOrderNumber(),
                getCustomerName(),
                getStateTax().getStateAbbreviation(),
                getStateTax().getTaxRate(),
                getProduct().getProductType(),
                getArea(),
                getProduct().getCostPerSqft(),
                getProduct().getLaborCostPerSqft(),
                getMaterialCost(),
                getLaborCost(),
                getTax(),
                getTotal());
    }
}

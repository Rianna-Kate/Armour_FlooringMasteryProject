package com.sg.flooring.service;

import com.sg.flooring.dto.Order;
import com.sg.flooring.dto.Product;
import com.sg.flooring.dto.StateTax;

import java.time.LocalDate;
import java.util.List;

public interface FlooringServiceLayer {
    public Product getProduct(String productType);
    public List<Product> getProducts();
    public StateTax getStateTax(String stateAbbreviation);
    public List<StateTax> getStateTaxes();
    public List<Order> getAllOrders();
    public List<Order> getAllOrders(LocalDate orderDate);
    public Order addOrder(Order order);
    public Order getOrder(LocalDate orderDate, int orderNumber);
    public Order editOrder(Order order);
    public Order removeOrder(LocalDate orderDate, int orderNumber);
    public int getTotalOrderCount();



}

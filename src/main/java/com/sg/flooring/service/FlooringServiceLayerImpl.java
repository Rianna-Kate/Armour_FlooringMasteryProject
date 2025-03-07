package com.sg.flooring.service;

import com.sg.flooring.dao.*;
import com.sg.flooring.dto.Order;
import com.sg.flooring.dto.Product;
import com.sg.flooring.dto.StateTax;

import java.time.LocalDate;
import java.util.List;

public class FlooringServiceLayerImpl implements FlooringServiceLayer {
    OrderDao orderDao;
    ProductDao productDao;
    StateTaxDao stateTaxDao;

    public FlooringServiceLayerImpl(OrderDao orderDao, ProductDao productDao, StateTaxDao stateTaxDao) {
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.stateTaxDao = stateTaxDao;
    }

    public Order validateOrderData(Order order)
            throws OrderDataValidationException {
        if (order.getCustomerName() == null ||
                order.getCustomerName().trim().isEmpty() ||
                order.getProduct() == null ||
                order.getStateTax() == null ||
                order.getArea() == null) {
            throw new OrderDataValidationException("ERROR: Fields [Customer" +
                    "Name, Product Type, State Tax, and Area] is required.");
        }
        return order;
    }

    @Override
    public Product getProduct(String productType)
            throws ProductNotFoundException {
        return productDao.getProduct(productType);
    }

    @Override
    public List<Product> getProducts()
            throws FlooringDataPersistenceException {
        return productDao.getAllProducts();
    }

    @Override
    public StateTax getStateTax(String stateAbbreviation)
            throws StateTaxNotFoundException {
        return stateTaxDao.getStateTax(stateAbbreviation);
    }

    @Override
    public List<StateTax> getStateTaxes()
            throws FlooringDataPersistenceException {
        return stateTaxDao.getAllStateTaxes();
    }

    @Override
    public List<Order> getAllOrders()
            throws FlooringDataPersistenceException {
        return orderDao.getAllOrders();
    }

    @Override
    public List<Order> getAllOrders(LocalDate orderDate)
            throws FlooringDataPersistenceException {
        return orderDao.getAllOrders(orderDate);
    }

    @Override
    public Order addOrder(Order order)
            throws FlooringDataPersistenceException {
        return orderDao.addOrder(validateOrderData(order));
    }

    @Override
    public Order getOrder(LocalDate orderDate, int orderNumber)
            throws OrderNotFoundException {
        return orderDao.getOrder(orderDate, orderNumber);
    }

    @Override
    public Order editOrder(Order order)
            throws FlooringDataPersistenceException {
        return orderDao.editOrder(order);
    }

    @Override
    public Order removeOrder(LocalDate orderDate, int orderNumber)
            throws FlooringDataPersistenceException {
        return orderDao.removeOrder(orderDate, orderNumber);
    }

    @Override
    public int getTotalOrderCount()
            throws FlooringDataPersistenceException {
        return orderDao.getAllOrders().size();
    }
}

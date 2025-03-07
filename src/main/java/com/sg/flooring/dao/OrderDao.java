package com.sg.flooring.dao;

import com.sg.flooring.dto.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderDao {
    List<Order> getAllOrders();
    List<Order> getAllOrders(LocalDate orderDate);

    Order addOrder(Order order);

    Order getOrder(LocalDate orderDate, int orderNumber);

    Order editOrder(Order order);

    Order removeOrder(LocalDate orderDate, int orderNumber);
}

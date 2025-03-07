package com.sg.flooring.dao;

import com.sg.flooring.dto.Order;
import com.sg.flooring.dto.Product;
import com.sg.flooring.dto.StateTax;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

class OrderDaoFileImplTest {
    OrderDao orderDao;
    private Path directoryPath;

    public OrderDaoFileImplTest () {
    }

    @BeforeEach
    public void setUp() throws Exception {

        // This code was given by ChatGPT. I know how to delete a file correctly but not a folder and it's contents.
        directoryPath = Paths.get("Orders");
        if (Files.exists(directoryPath) && Files.isDirectory(directoryPath)) {
            // Recursively delete the directory contents
            Files.walk(directoryPath)
                    .map(Path::toFile)
                    .forEach(File::delete);
        }

        orderDao = new OrderDaoFileImpl();
    }

    @Test
    void testGetAllOrders() {
        Order order = new Order("customer1");
        order.setOrderDate(LocalDate.now().plusDays(20));
        order.setOrderNumber(4);
        order.setProduct(new Product("Wood", "5.15", "4.75"));
        order.setStateTax(new StateTax("TX", "Texas", "4.45"));
        order.setArea(new BigDecimal(100));
        orderDao.addOrder(order);

        Order order1 = new Order("customer2");
        order1.setOrderNumber(5);
        order1.setOrderDate(LocalDate.now().plusDays(20));
        order1.setProduct(new Product("Wood", "5.15", "4.75"));
        order1.setStateTax(new StateTax("TX", "Texas", "4.45"));
        order1.setArea(new BigDecimal(100));
        orderDao.addOrder(order1);

        Assertions.assertEquals(2, orderDao.getAllOrders().size(), "Size should be 2, since 2 orders added");
    }

    @Test
    public void testGetOrderWithDateAndNumber() {
        Order order = new Order("customer");

        order.setOrderDate(LocalDate.now().minusDays(2));
        order.setProduct(new Product("Wood", "5.15", "4.75"));
        order.setStateTax(new StateTax("TX", "Texas", "4.45"));
        order.setArea(new BigDecimal(100));
        orderDao.addOrder(order);

        assertNotNull("Object should not be null", orderDao.getOrder(LocalDate.now(), 0));
    }

    @Test
    public void testGetOrderWithDateAndWrongNumber() {
        Order order = new Order("customer");
        order.setOrderNumber(12);
        order.setOrderDate(LocalDate.now().minusDays(31));
        order.setProduct(new Product("Wood", "5.15", "4.75"));
        order.setStateTax(new StateTax("TX", "Texas", "4.45"));
        order.setArea(new BigDecimal(100));
        orderDao.addOrder(order);

        assertNull("Object should be null", orderDao.getOrder(LocalDate.now().minusDays(31), 1));
    }
}
package com.sg.flooring.service;

import com.sg.flooring.dao.*;
import com.sg.flooring.dto.Order;
import com.sg.flooring.dto.Product;
import com.sg.flooring.dto.StateTax;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class FlooringServiceLayerImplTest {
    private FlooringServiceLayer service;

    public FlooringServiceLayerImplTest () {
        ProductDao productDao = new ProductDaoFileImpl();
        StateTaxDao stateTaxDao = new StateTaxDaoFileImpl();
        OrderDao orderDao = new OrderDaoFileImpl();

        service = new FlooringServiceLayerImpl(orderDao, productDao, stateTaxDao);
    }

    @Test
    public void validateOrderData () {
        Order order = new Order("customer");
        order.setOrderNumber(0);
        order.setOrderDate(LocalDate.now());
        order.setProduct(new Product("Wood", "5.15", "4.75"));
        order.setStateTax(new StateTax("TX", "Texas", "4.45"));
        order.setArea(new BigDecimal(100));
        assertDoesNotThrow(() -> service.addOrder(order), "The validate OrderData should not be thrown.");
    }

    @Test
    public void validateOrderDataExceptionThrown () {
        Order order = new Order("");
        order.setOrderNumber(1);
        order.setOrderDate(LocalDate.now());
        order.setProduct(new Product("Wood", "5.15", "4.75"));
        order.setStateTax(new StateTax("TX", "Texas", "4.45"));
        order.setArea(new BigDecimal(100));
        assertThrows(OrderDataValidationException.class, () -> {
            service.addOrder(order);
        });
    }

    @Test
    public void testGetProduct() {
        Order order = new Order("customer");
        order.setOrderNumber(2);
        order.setOrderDate(LocalDate.now());
        order.setStateTax(new StateTax("TX", "Texas", "4.45"));
        order.setArea(new BigDecimal(100));

        Product product = new Product("Wood", "5.15", "4.75");
        order.setProduct(product);

        assertNotNull("Product not be null", order.getProduct());
    }


    @Test
    public void testGetProducts() {
        // Products.txt file has 4 items written inside
        assertEquals(4, service.getProducts().size());
    }

    @Test
    public void testGetStateTax() {
        Order order = new Order("customer");
        order.setOrderNumber(2);
        order.setOrderDate(LocalDate.now());
        order.setProduct(new Product("Wood", "5.15", "4.75"));
        StateTax stateTax = new StateTax("TX", "Texas", "4.45");
        order.setStateTax(stateTax);
        order.setArea(new BigDecimal(100));

        assertNotNull(order.getStateTax());
    }

    @Test
    public void testGetStateTaxes() {
        // Products.txt file has 4 items written inside
        assertEquals(4, service.getStateTaxes().size());
    }

/*    @Test
    public void testGetAllOrders() {
        Order order = new Order("customer1");
        order.setOrderDate(LocalDate.now().plusDays(20));
        order.setOrderNumber(4);
        order.setProduct(new Product("Wood", "5.15", "4.75"));
        order.setStateTax(new StateTax("TX", "Texas", "4.45"));
        order.setArea(new BigDecimal(100));
        service.addOrder(order);

        Order order1 = new Order("customer2");
        order1.setOrderNumber(5);
        order1.setOrderDate(LocalDate.now().plusDays(20));
        order1.setProduct(new Product("Wood", "5.15", "4.75"));
        order1.setStateTax(new StateTax("TX", "Texas", "4.45"));
        order1.setArea(new BigDecimal(100));
        service.addOrder(order1);

        assertEquals("Size should be 2, since 2 orders added", 2, service.getAllOrders().size());
    }
 */

    @Test
    public void testGetAllOrdersWithDate() {
        Order order = new Order("customer1");
        order.setOrderNumber(6);
        order.setOrderDate(LocalDate.now().plusDays(4));
        order.setProduct(new Product("Wood", "5.15", "4.75"));
        order.setStateTax(new StateTax("TX", "Texas", "4.45"));
        order.setArea(new BigDecimal(100));
        service.addOrder(order);

        Order order1 = new Order("customer2");
        order1.setOrderNumber(7);
        order1.setOrderDate(LocalDate.now().plusDays(5));
        order1.setProduct(new Product("Wood", "5.15", "4.75"));
        order1.setStateTax(new StateTax("TX", "Texas", "4.45"));
        order1.setArea(new BigDecimal(100));
        service.addOrder(order1);


        assertEquals("Size should be 1, since both dates of orders are different.", 1, service.getAllOrders(LocalDate.now()).size());
    }

    /*
    @Test
    public void testGetOrderWithDateAndNumber() {
        Order order = new Order("customer");

        order.setOrderDate(LocalDate.now().minusDays(2));
        order.setProduct(new Product("Wood", "5.15", "4.75"));
        order.setStateTax(new StateTax("TX", "Texas", "4.45"));
        order.setArea(new BigDecimal(100));
        service.addOrder(order);

        assertNotNull("Object should not be null", service.getOrder(LocalDate.now(), 0));
    }
     */

/*
    @Test
    public void testGetOrderWithDateAndWrongNumber() {
        Order order = new Order("customer");
        order.setOrderNumber(12);
        order.setOrderDate(LocalDate.now().minusDays(31));
        order.setProduct(new Product("Wood", "5.15", "4.75"));
        order.setStateTax(new StateTax("TX", "Texas", "4.45"));
        order.setArea(new BigDecimal(100));
        service.addOrder(order);

        assertNull("Object should be null", service.getOrder(LocalDate.now().minusDays(31), 1));
    }
 */

    @Test
    public void testEditOrder() {
        Order order = new Order("customer");
        order.setOrderDate(LocalDate.now().plusDays(2));
        order.setProduct(new Product("Wood", "5.15", "4.75"));
        order.setStateTax(new StateTax("TX", "Texas", "4.45"));
        order.setArea(new BigDecimal(100));
        service.addOrder(order);

        // Generally the view would do this, but for testing purposes, it's hard coded.
        Order clone = new Order(order.getCustomerName());
        clone.setCustomerName(order.getCustomerName());
        clone.setProduct(order.getProduct());
        clone.setArea(order.getArea());
        clone.setOrderDate(order.getOrderDate());
        clone.setOrderNumber(order.getOrderNumber());
        clone.setStateTax(order.getStateTax());

        String oldName = order.getCustomerName();

        order.setCustomerName("Jules");

        service.editOrder(order);

        assertNotEquals(order, clone);
        assertNotEquals(order.getCustomerName(), oldName);
        assertEquals("Should be equal since orderNumber will never be effected when edited.", clone.getOrderNumber(), order.getOrderNumber());
    }

    @Test
    public void testRemoveOrder () {
        Order order = new Order("customer");
        order.setOrderNumber(22);
        order.setOrderDate(LocalDate.now().plusDays(19));
        order.setProduct(new Product("Wood", "5.15", "4.75"));
        order.setStateTax(new StateTax("TX", "Texas", "4.45"));
        order.setArea(new BigDecimal(100));
        service.addOrder(order);
        int originalCount = service.getAllOrders().size();
        service.removeOrder(order.getOrderDate(), order.getOrderNumber());

        assertNotEquals(originalCount, service.getAllOrders().size());
    }

    @Test
    public void testGetTotalOrderCount () {
        Order order = new Order("customer");
        order.setOrderNumber(16);
        order.setOrderDate(LocalDate.now().plusDays(19));
        order.setProduct(new Product("Wood", "5.15", "4.75"));
        order.setStateTax(new StateTax("TX", "Texas", "4.45"));
        order.setArea(new BigDecimal(100));

        service.addOrder(order);
        int originalCount = service.getTotalOrderCount();
        System.out.println(originalCount);

        service.removeOrder(order.getOrderDate(), order.getOrderNumber());
        int newCount = service.getTotalOrderCount();
        System.out.println(newCount);

        assertNotEquals(originalCount, newCount);
    }

}
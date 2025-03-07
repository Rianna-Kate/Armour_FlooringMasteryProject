package com.sg.flooring.controller;

import com.sg.flooring.dao.*;
import com.sg.flooring.dto.Order;
import com.sg.flooring.dto.Product;
import com.sg.flooring.service.FlooringServiceLayer;
import com.sg.flooring.ui.FlooringView;
import com.sg.flooring.ui.MenuSelection;
import com.sg.flooring.ui.UserIO;
import com.sg.flooring.ui.UserIOConsoleImpl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class FlooringController {
    private FlooringView view;
    private FlooringServiceLayer service;

    public FlooringController(FlooringServiceLayer service, FlooringView view) {
        this.service = service;
        this.view = view;
    }

    public void run () {
        boolean keepGoing = true;
         while (keepGoing) {
             MenuSelection menuSelection = promptMenuSelection();
             switch (menuSelection) {
                 case DISPLAY_ORDERS:
                     displayOrders();
                     break;
                 case ADD_ORDER:
                     addOrder();
                     break;
                 case EDIT_ORDER:
                     editOrder();
                     break;
                 case REMOVE_ORDER:
                     removeOrder();
                     break;
                 case EXPORT_ALL_DATA:
                     view.exportAllData(); //TODO
                     break;
                 case EXIT:
                     keepGoing = false;
                     break;
             }
         }
    }

    private MenuSelection promptMenuSelection() {
        return view.displayMenuAndGet();
    }

    private void displayOrders() throws
            FlooringDataPersistenceException {
        LocalDate userDate = view.displayFindOrders();
        List<Order> orderList = service.getAllOrders(userDate);
        view.displayOrders(orderList);
    }

    private void addOrder() throws
            FlooringDataPersistenceException {
        Order newOrder = view.displayAddOrder(service.getProducts(), service.getStateTaxes());
        service.addOrder(newOrder);
    }

    private void editOrder() throws
            FlooringDataPersistenceException,
            OrderNotFoundException,
            ProductNotFoundException,
            StateTaxNotFoundException {
        Order orderToFind = view.displayFindOrder();
        Order orderToEdit = null;

        if (!service.getAllOrders(orderToFind.getOrderDate()).isEmpty()) {
            orderToEdit = service.getOrder(orderToFind.getOrderDate(), orderToFind.getOrderNumber());
        }

        view.displayOrder(orderToEdit);

        if (orderToEdit != null) {
            orderToEdit = view.displayEditOrder(orderToEdit, service.getProducts(), service.getStateTaxes());
            service.editOrder(orderToEdit);
        }
    }

    private void removeOrder() throws
            FlooringDataPersistenceException {
        Order orderToFind = view.displayFindOrder();
        Order removedOrder = null;

        if (!service.getAllOrders(orderToFind.getOrderDate()).isEmpty()) {
            removedOrder = service.getOrder(orderToFind.getOrderDate(), orderToFind.getOrderNumber());
        }

        removedOrder = view.displayRemoveOrder(removedOrder);

        if (removedOrder != null) {
            service.removeOrder(removedOrder.getOrderDate(), removedOrder.getOrderNumber());
        }
    }
}

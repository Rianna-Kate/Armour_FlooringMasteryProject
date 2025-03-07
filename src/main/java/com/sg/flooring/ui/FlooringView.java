package com.sg.flooring.ui;

import com.sg.flooring.dto.Order;
import com.sg.flooring.dto.Product;
import com.sg.flooring.dto.StateTax;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FlooringView {
    private UserIO io;

    public FlooringView (UserIO io) {
        this.io = io;
    }

    private void displayHeader(String title) {
        io.print(title);
    }

    public MenuSelection displayMenuAndGet() {
        final MenuSelection[] values = MenuSelection.values();
        displayHeader("\n-------MENU-------");
        for (int i = 1; i < values.length; i++) {
            io.print(i + ". " + values[i]);
        }
        return MenuSelection
                .fromInt(io.readInt("Please select an option 1 through " + (values.length - 1), 1, values.length - 1));
    }

    /**
     * Differing from UML diagram takes in an order to remove and checks if not null
     * If not null, then user is prompted to confirm before deletion.
     * @param orderToRemove
     * @return Order to be removed from the order hashmap
     */
    public Order displayRemoveOrder(Order orderToRemove) {
        displayHeader("-------Chosen Order-------");
        displayOrder(orderToRemove);
        displayHeader("--------------");
        if (orderToRemove != null) {
            boolean userChoice = io.readBoolean("Would you like to remove the order from the system? " +
                    "Please input: Yes/No");

            if (!userChoice) {
                orderToRemove = null;
            }
        }
        return orderToRemove;
    }

    /**
     * Prompts user for the variables that can be changed in the Order object
     * If they do not put any input, it remains the same as it's cloned version.
     * @param order
     * @param products
     * @param stateTaxes
     * @return Order object clone regardless if edited or not
     */
    public Order displayEditOrder(Order order, List<Product> products, List<StateTax> stateTaxes) { // TODO Must fix to use string not numbers for state and products
        Order editedOrder = new Order(order.getCustomerName());
        editedOrder.setCustomerName(order.getCustomerName());
        editedOrder.setProduct(order.getProduct());
        editedOrder.setArea(order.getArea());
        editedOrder.setOrderDate(order.getOrderDate());
        editedOrder.setOrderNumber(order.getOrderNumber());
        editedOrder.setStateTax(order.getStateTax());

        displayHeader("\n-------ORDER EDIT-------");

        String customerName = io.readString("Enter customer name (" + order.getCustomerName() + "): ");
        if (!customerName.equals(order.getCustomerName()) && !customerName.isEmpty()) {
            editedOrder.setCustomerName(customerName);
        }

        displayStateTaxes(stateTaxes);
        String stateAbbreviation = io.readString("Enter the STATE ABBREVIATION of the state you wish to purchase from:").toUpperCase();
        StateTax selectedState = editedOrder.getStateTax();
        boolean stateAbbreviationNotFound = false;
        while (!stateAbbreviationNotFound) {
            for (StateTax state : stateTaxes) {
                if (stateAbbreviation.isEmpty()) {
                    stateAbbreviationNotFound = true;
                }
                if (state.getStateAbbreviation().equalsIgnoreCase(stateAbbreviation) && !stateAbbreviation.isEmpty()) {
                    selectedState = state;
                    stateAbbreviationNotFound = true;
                }
            }
            if (!stateAbbreviationNotFound) {
                displayStateAbbreviationNotFound(stateAbbreviation);
                stateAbbreviation = io.readString("Enter the STATE ABBREVIATION of the state you wish to purchase from:").toUpperCase();
            }
        }

        if (!selectedState.equals(order.getStateTax()) && !stateAbbreviation.isEmpty()) {
            editedOrder.setStateTax(selectedState);
        }

        displayProducts(products);
        String productType = io.readString("Enter the PRODUCT you wish to purchase:");
        Product selectedProduct = editedOrder.getProduct();
        boolean productTypeNotFound = false;
        while (!productTypeNotFound) {
            for (Product product : products) {
                if (productType.isEmpty()) {
                    productTypeNotFound = true;
                }
                if (product.getProductType().equalsIgnoreCase(productType) && !productType.isEmpty()) {
                    selectedProduct = product;
                    productTypeNotFound = true;
                }
            }
            if (!productTypeNotFound) {
                displayProductTypeNotFound(productType);
                productType = io.readString("Enter the STATE ABBREVIATION of the state you wish to purchase from:").toUpperCase();
            }
        }

        if (!selectedProduct.equals(order.getProduct()) && !productType.isEmpty()) {
            editedOrder.setProduct(selectedProduct);
        }

        BigDecimal area = promptBigDecimal();

        if (!area.equals(order.getArea())) {
            editedOrder.setArea(area);
        }

        return editedOrder;
    }

    public Order displayFindOrder() {
        displayHeader("\n-------FIND ORDER-------");
        LocalDate userDate = promptOrderDate();
        int userNumber = promptOrderNumber();
        Order orderToLookFor = new Order();
        orderToLookFor.setOrderNumber(userNumber);
        orderToLookFor.setOrderDate(userDate);

        return orderToLookFor;
    }

    public LocalDate displayFindOrders() {
        return promptOrderDate();
    }

    public void displayOrder (Order order) {
        displayHeader("\n-------ORDER DISPLAY-------");
        if (order!= null) {
            io.print(order.toString());
        }
        else {
            io.print("Order does not exist in system.");
            displayPressEnterToContinue();
        }
    }

    public void displayOrders(List<Order> orders) {
        displayHeader("\n-------ORDER LIST-------");
        if (!orders.isEmpty()) {
            for (Order order : orders) {
                io.print(order.toString());
            }
        }
        else {
            io.print("Error. No orders for date the printed date.");
        }
        displayPressEnterToContinue();
    }

    /**
     * Propmpt for creating an order an assignined needed variables to
     * create Order object
     * @param products
     * @param states
     * @return
     */
    public Order displayAddOrder(List<Product> products, List<StateTax> states) {
        displayHeader("\n-------ORDER CREATION-------");
        LocalDate orderDate = io.readLocalDate("Enter a date.", LocalDate.now());
        String customerName = io.readString("Enter a customer name: ");

        displayStateTaxes(states);
        String stateAbbreviation = io.readString("Enter the STATE ABBREVIATION of the state you wish to purchase from:").toUpperCase();
        StateTax selectedState = null;
        boolean stateAbbreviationNotFound = false;
        while (!stateAbbreviationNotFound) {
            for (StateTax state : states) {
                if (state.getStateAbbreviation().equalsIgnoreCase(stateAbbreviation)) {
                    selectedState = state;
                    stateAbbreviationNotFound = true;
                }
            }
            if (!stateAbbreviationNotFound) {
                displayStateAbbreviationNotFound(stateAbbreviation);
                stateAbbreviation = io.readString("Enter the STATE ABBREVIATION of the state you wish to purchase from:").toUpperCase();
            }
        }

        displayProducts(products);
        String productType = io.readString("Enter the PRODUCT you wish to purchase:");
        Product selectedProduct = null;
        boolean productTypeNotFound = false;
        while (!productTypeNotFound) {
            for (Product product : products) {
                if (product.getProductType().equalsIgnoreCase(productType)) {
                    selectedProduct = product;
                    productTypeNotFound = true;
                }
            }
            if (!productTypeNotFound) {
                displayProductTypeNotFound(productType);
                productType = io.readString("Enter the STATE ABBREVIATION of the state you wish to purchase from:").toUpperCase();
            }
        }

        BigDecimal area = promptBigDecimal();

        // Setting order
        Order currOrder = new Order(customerName);
        currOrder.setOrderDate(orderDate);
        currOrder.setProduct(selectedProduct);
        currOrder.setStateTax(selectedState);
        currOrder.setArea(area);

        return currOrder;
    }

    public void displayProducts(List<Product> products) {
        displayHeader("\n-------PRODUCT LIST-------");
        int counter = 1;
        for (Product product : products) {
            io.print(counter + ". " + product.toString());
            counter++;
        }
    }

    public void displayStateTaxes(List<StateTax> states) {
        displayHeader("\n-------STATE LIST-------");
        int counter = 1;
        for (StateTax state : states) {
            io.print(counter + ". " + state.toString());
            counter++;
        }
    }

    public String exportAllData () {
        io.print("DATA EXPORT NOT IMPLEMENTED.");
        displayPressEnterToContinue();
        return "";
    }

    private LocalDate promptOrderDate() {
        return io.readLocalDate("Please enter a date in the format: (mm/dd/yyyy)");
    }

    private BigDecimal promptBigDecimal() {
        return io.readBigDecimal("Enter the size of area (in sqft)", 2, new BigDecimal(100));
    }

    private int promptOrderNumber() {
        return io.readInt("Please enter an order number:");
    }

    private void displayPressEnterToContinue() {
        io.readString("Hit Enter to Continue...");
    }

    public void displayStateAbbreviationNotFound (String stateAbbreviation) {
        io.print("Invalid State. Try again.");
    }

    public void displayProductTypeNotFound (String productType) {
        io.print("Invalid Product Type. Try again.");
    }
}

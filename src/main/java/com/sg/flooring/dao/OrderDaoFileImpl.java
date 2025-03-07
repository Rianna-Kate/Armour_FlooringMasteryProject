package com.sg.flooring.dao;

import com.sg.flooring.dto.Order;
import com.sg.flooring.dto.Product;
import com.sg.flooring.dto.StateTax;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class OrderDaoFileImpl implements OrderDao {
    private final String FILE_NAME;
    private static String DELIMITER = ",";

    Map<Integer, Order> orders = new HashMap<>();

    public OrderDaoFileImpl () {
        FILE_NAME = "Orders_";
    }

    public OrderDaoFileImpl (String textFile) {
        FILE_NAME = textFile;
    }


    @Override
    public List<Order> getAllOrders() throws
            FlooringDataPersistenceException {
        return new ArrayList<Order>(orders.values());
    }

    @Override
    public List<Order> getAllOrders(LocalDate orderDate) throws
            FlooringDataPersistenceException {
        ArrayList<Order> ordersByDate = orders.values().stream()
                .filter(orders -> orders.getOrderDate().equals(orderDate))
                .collect(Collectors.toCollection(ArrayList::new));

        return ordersByDate;
    }

    @Override
    public Order addOrder(Order order) throws
            FlooringDataPersistenceException {
        read();
        int orderNum = order.getOrderNumber();
        while (orders.containsKey(orderNum)) {
            orderNum++;
        }
        order.setOrderNumber(orderNum);
        Order prevOrder = orders.put(orderNum, order);
        write();
        return prevOrder;
    }

    @Override
    public Order getOrder(LocalDate orderDate, int orderNumber) throws
            OrderNotFoundException {
        if (orders.containsKey(orderNumber)) {
            return orders.get(orderNumber);
        }
        return null;
    }

    @Override
    public Order editOrder(Order order) throws
            FlooringDataPersistenceException {
        Order editedOrder = orders.put(order.getOrderNumber(), order);
        return editedOrder;
    }

    @Override
    public Order removeOrder(LocalDate orderDate, int orderNumber) throws
            FlooringDataPersistenceException {
        if (orders.containsKey(orderNumber)) {
            return orders.remove(orderNumber);
        }
        return null;
    }

    private Order unmarshallOrder(String orderAsText) {
        String[] orderTokens = orderAsText.split(DELIMITER);
        String orderNumber = orderTokens[0];
        String customerName = orderTokens[1];
        String stateName = orderTokens[2];
        String stateTaxRate = orderTokens[3];
        String productType = orderTokens[4];
        String area = orderTokens[5];
        String costPerSqft = orderTokens[6];
        String laborCostPerSqft = orderTokens[7];
        String materialCost = orderTokens[8];
        String laborCost = orderTokens[9];
        String tax = orderTokens[10];
        String total = orderTokens[11];

        Product productFromFile = new Product(productType, costPerSqft, laborCostPerSqft);
        StateTax stateTaxFromFile = new StateTax(stateName, null, stateTaxRate);
        Order orderFromFile = new Order(customerName);
        orderFromFile.setProduct(productFromFile);
        orderFromFile.setStateTax(stateTaxFromFile);
        orderFromFile.setArea(new BigDecimal(area));
        orderFromFile.setOrderNumber(Integer.parseInt(orderNumber));


        return orderFromFile;
    }

    private String marshallOrder(Order anOrder) {
        return anOrder.toString();
    }

    private void read()
            throws FlooringDataPersistenceException {
        Scanner scanner;

        File folder = new File("Orders");


        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".txt")) {
                        System.out.println("Reading file: " + file.getName());

                        try {
                            scanner = new Scanner(
                                    new BufferedReader(
                                            new FileReader(file)));
                        } catch (FileNotFoundException e) {
                            throw new FlooringDataPersistenceException(
                                    "-_- Could not load order file data into memory.", e);
                        }


                        String currentLine;

                        Order currentOrder;
                        while (scanner.hasNextLine()) {
                            currentLine = scanner.nextLine();

                            // unmarshall the line into an Order
                            currentOrder = unmarshallOrder(currentLine);
                            currentOrder.setOrderDate(LocalDate.parse(file.getName().substring(7, 15), DateTimeFormatter.ofPattern("MMddyyyy")));

                            orders.put(currentOrder.getOrderNumber(), currentOrder);
                        }

                        scanner.close();
                    }
                }
            }
        } else {
            System.out.println("The specified folder does not exist or is not a directory.");
        }
    }

    private void write() throws FlooringDataPersistenceException {
        File folder = new File("Orders");

        if (!folder.exists()) {
            folder.mkdir();
        }

        File file = new File(folder, FILE_NAME);

        PrintWriter out = null;
        HashSet<LocalDate> orderDates = new HashSet<>();
        List<Order> orderList = this.getAllOrders();
        HashSet<LocalDate> dates = new HashSet<>();

        for (Order order : orderList) {
            dates.add(order.getOrderDate());
        }
        System.out.println(dates.size());

        try {
            for (LocalDate currentDate : dates) {
                List<Order> orderListByDate = this.getAllOrders(currentDate);
                out = new PrintWriter(new FileWriter(file + currentDate.format(DateTimeFormatter.ofPattern("MMddyyyy")) + ".txt"));

                for (Order currentOrder : orderListByDate) {
                    String ordersAsText = marshallOrder(currentOrder);
                    out.println(ordersAsText);
                    out.flush();
                }
            }
        } catch (IOException e) {
            throw new FlooringDataPersistenceException(
                    "Could not save order data.", e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}

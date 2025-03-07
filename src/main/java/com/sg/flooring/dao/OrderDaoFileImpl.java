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

    /**
     * Gets All orders within orders hashmap
     * @return list of all Order objects
     * @throws FlooringDataPersistenceException
     */
    @Override
    public List<Order> getAllOrders() throws
            FlooringDataPersistenceException {
        return new ArrayList<Order>(orders.values());
    }

    /**
     * Used to get Local Dates based on a specific date through lambda stream.
     * @param orderDate
     * @return list of orders on a specific date
     * @throws FlooringDataPersistenceException
     */
    @Override
    public List<Order> getAllOrders(LocalDate orderDate) throws
            FlooringDataPersistenceException {
        ArrayList<Order> ordersByDate = orders.values().stream()
                .filter(orders -> orders.getOrderDate().equals(orderDate))
                .collect(Collectors.toCollection(ArrayList::new));

        return ordersByDate;
    }

    /**
     * Adding order to hashmap
     * @param order - The Order to be processed and put into the order hashmap
     * @return
     * @throws FlooringDataPersistenceException
     */
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

    /**
     * Get order based on an order date and an order number.
     * @param orderDate
     * @param orderNumber
     * @return An existing object if it exists within hashmap, otherwise null
     * @throws OrderNotFoundException
     */
    @Override
    public Order getOrder(LocalDate orderDate, int orderNumber) throws
            OrderNotFoundException {
        if (orders.containsKey(orderNumber)) {
            return orders.get(orderNumber);
        }
        return null;
    }

    /**
     * Edit order if it exists in the system.
     * @param order
     * @return Order object that is either edited or not.
     * @throws FlooringDataPersistenceException
     */
    @Override
    public Order editOrder(Order order) throws
            FlooringDataPersistenceException {
        Order editedOrder = orders.put(order.getOrderNumber(), order);
        return editedOrder;
    }

    /**
     * Order object will be removed from the order hashmap if it exists.
     * @param orderDate
     * @param orderNumber
     * @return Order object that is to be removed or null if it does not exist.
     * @throws FlooringDataPersistenceException
     */
    @Override
    public Order removeOrder(LocalDate orderDate, int orderNumber) throws
            FlooringDataPersistenceException {
        if (orders.containsKey(orderNumber)) {
            return orders.remove(orderNumber);
        }
        return null;
    }

    /**
     * Reads each line of text, seperating into tokens that are then assigned to Order Object
     * variables to create and add an Order object to the hashmap.
     * @param orderAsText
     * @return Order Object that was specified in a line in the text file
     */
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

    /**
     * Organize variables of Order object into text so that it can be written to a file.
     * @param anOrder
     * @return Formatted String
     */
    private String marshallOrder(Order anOrder) {
        return anOrder.toString();
    }

    /**
     * Reading all Orders from the files in each file specified by "Objects_" and then defined by the
     * date as instructed.
     * @throws FlooringDataPersistenceException
     */
    private void read()
            throws FlooringDataPersistenceException {
        Scanner scanner;

        File folder = new File("Orders");

        // Looking for the Orders folder and if there is files will separate.
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

    /**
     * Writing Order objects to text files specified by LocalDates
     * @throws FlooringDataPersistenceException
     */
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

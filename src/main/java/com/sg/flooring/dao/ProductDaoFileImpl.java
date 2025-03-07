package com.sg.flooring.dao;

import com.sg.flooring.dto.Product;

import java.io.*;
import java.util.*;

public class ProductDaoFileImpl implements ProductDao{
    private static final String FILE_NAME = "Products.txt";
    private static final String DELIMITER = ",";

    Map<String, Product> products = new HashMap<>();

    public ProductDaoFileImpl() {
        read();
    }

    /**
     * Reads each line of text, separating into tokens that are then assigned to Product Object
     * variables to create and add a Product object to the hashmap.
     * @param productsAsText
     * @return
     */
    private Product unmarshallProduct(String productsAsText) {
        String[] productTokens = productsAsText.split(DELIMITER);
        String productType = productTokens[0];
        String costPerSqft = productTokens[1];
        String laborCostPerSqft = productTokens[2];


        Product productFromFile = new Product(productType, costPerSqft, laborCostPerSqft);

        return productFromFile;
    }

    /**
     * Organize variables of Product object into text so that it can be written to a file.
     * @param aProduct
     * @return
     */
    private String marshallProduct(Product aProduct) {
        String productAsText = aProduct.getProductType() + DELIMITER;
        productAsText += aProduct.getCostPerSqft() + DELIMITER;
        productAsText += aProduct.getLaborCostPerSqft() + DELIMITER;

        return productAsText;
    }

    /**
     * Reading all Products objects from the file "Products.txt" file
     * @throws FlooringDataPersistenceException
     */
    private void read()
            throws FlooringDataPersistenceException {
        Scanner scanner;

        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(FILE_NAME)));
        } catch (FileNotFoundException e) {
            throw new FlooringDataPersistenceException(
                    "-_- Could not load product file data into memory.", e);
        }
        String currentLine;

        Product currentProduct;
        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();

            // unmarshall the line into a Product
            currentProduct = unmarshallProduct(currentLine);

            products.put(currentProduct.getProductType(), currentProduct);
        }

        scanner.close();
    }

    // Never used
    private void write() throws FlooringDataPersistenceException {
        PrintWriter out;

        try {
            out = new PrintWriter(new FileWriter(FILE_NAME));
        } catch (IOException e) {
            throw new FlooringDataPersistenceException(
                    "Could not save product data.", e);
        }

        String productsAsText;
        List<Product> productList = this.getAllProducts();
        for (Product currentProduct : productList) {
            productsAsText = marshallProduct(currentProduct);
            out.println(productsAsText);
            out.flush();
        }

        out.close();
    }

    /**
     * Get Product object based on specified text.
     * @param productType - Type of Product to be sold. (Ex. Wood, Laminate, etc.)
     * @return Product object based on product type.
     * @throws ProductNotFoundException
     */
    @Override
    public Product getProduct(String productType)
    throws ProductNotFoundException {
        return products.get(productType);
    }

    /**
     * Get the list of all Product objects in the products hashmap
     * @return List of all Product Objects within the products hashmap
     * @throws FlooringDataPersistenceException
     */
    @Override
    public List<Product> getAllProducts()
            throws FlooringDataPersistenceException {
        return new ArrayList<Product>(products.values());
    }
}

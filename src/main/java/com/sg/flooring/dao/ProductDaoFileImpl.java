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

    private Product unmarshallProduct(String productsAsText) {
        String[] productTokens = productsAsText.split(DELIMITER);
        String productType = productTokens[0];
        String costPerSqft = productTokens[1];
        String laborCostPerSqft = productTokens[2];


        Product productFromFile = new Product(productType, costPerSqft, laborCostPerSqft);

        return productFromFile;
    }

    private String marshallProduct(Product aProduct) {
        String productAsText = aProduct.getProductType() + DELIMITER;
        productAsText += aProduct.getCostPerSqft() + DELIMITER;
        productAsText += aProduct.getLaborCostPerSqft() + DELIMITER;

        return productAsText;
    }

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

    @Override
    public Product getProduct(String productType)
    throws ProductNotFoundException {
        return products.get(productType);
    }

    @Override
    public List<Product> getAllProducts()
            throws FlooringDataPersistenceException {
        return new ArrayList<Product>(products.values());
    }
}

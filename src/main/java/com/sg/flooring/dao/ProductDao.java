package com.sg.flooring.dao;

import com.sg.flooring.dto.Product;

import java.util.List;

public interface ProductDao {
    Product getProduct(String productType);
    List<Product> getAllProducts();
}

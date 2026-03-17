package com.jitendra.productservice.service;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.jitendra.productservice.model.Product;

public interface ProductService {

    Product createProduct(Product product) throws ExecutionException, InterruptedException;

    Product updateProduct(Long id, Product product);

    Product getProductById(Long id);

    List<Product> getAllProducts();

    List<Product> getProductsByCategory(String category);

    List<Product> getProductsByBrand(String brand);

    List<Product> searchProducts(String name);

    List<Product> getProductsByPriceRange(double min, double max);

    List<Product> getTopRatedProducts();

    void deactivateProduct(Long id);
}
package com.jitendra.productservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.jitendra.productservice.model.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, Long> {

    // find by category
    List<Product> findByCategory(String category);

    // find by brand
    List<Product> findByBrand(String brand);

    // find by category and brand
    List<Product> findByCategoryAndBrand(String category, String brand);

    // price range search
    List<Product> findByPriceBetween(double minPrice, double maxPrice);

    // top rated products
    List<Product> findTop10ByOrderByRatingDesc();

    // active products
    List<Product> findByActiveTrue();

    // search by name (case insensitive)
    List<Product> findByNameContainingIgnoreCase(String name);

}
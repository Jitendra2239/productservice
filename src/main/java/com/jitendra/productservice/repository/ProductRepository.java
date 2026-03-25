package com.jitendra.productservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.jitendra.productservice.model.Product;

@Repository
public interface ProductRepository extends MongoRepository<Product, Long> {


    List<Product> findByCategory(String category);


    List<Product> findByBrand(String brand);


    List<Product> findByCategoryAndBrand(String category, String brand);


    List<Product> findByPriceBetween(double minPrice, double maxPrice);


    List<Product> findTop10ByOrderByRatingDesc();


    List<Product> findByActiveTrue();


    List<Product> findByNameContainingIgnoreCase(String name);

}
package com.jitendra.productservice.repository;



import com.jitendra.productservice.model.Product;


import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {

    Product findBySkuCode(String skuCode);

}
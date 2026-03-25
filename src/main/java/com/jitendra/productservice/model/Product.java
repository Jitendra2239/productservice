package com.jitendra.productservice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class Product {

    @Id
    private String product_id;
    private String name;
    private String category;
    private String brand;

    private double price;
    private double discount;

    private Map<String,Object> attributes;

    private List<String> images;

    private boolean active;

    private double rating;
    private int reviewCount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
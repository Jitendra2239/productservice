package com.jitendra.productservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequestDTO {

    private String name;
    private String description;
    private BigDecimal price;
    private String brand;
    private Long categoryId;

    // getters and setters
}
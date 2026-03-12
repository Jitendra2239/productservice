package com.jitendra.productservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductResponseDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String brand;
    private String categoryName;
    private List<ProductImageDTO> images;
    private List<ProductAttributeDTO> attributes;

    // getters and setters
}
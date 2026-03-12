package com.jitendra.productservice.dto;

import lombok.Data;

@Data
public class ProductAttributeDTO {

    private Long id;
    private String attributeName;
    private String attributeValue;

    // getters and setters
}
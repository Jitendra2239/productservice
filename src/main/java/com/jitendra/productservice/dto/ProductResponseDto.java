package com.jitendra.productservice.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDto {

    private String id;  // ⚠️ change to String if using ObjectId

    private String name;
    private String category;
    private String brand;

    private double price;
    private double discount;

    private Map<String, Object> attributes;
    private List<String> images;

    private boolean active;

    private double rating;
    private int reviewCount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
package com.jitendra.productservice.mapper;

import com.jitendra.productservice.dto.*;
import com.jitendra.productservice.model.Product;

import java.time.LocalDateTime;

public class ProductMapper {

    public static Product toEntity(ProductRequestDto dto) {

        return Product.builder()
                .name(dto.getName())
                .category(dto.getCategory())
                .brand(dto.getBrand())
                .price(dto.getPrice())
                .discount(dto.getDiscount())
                .attributes(dto.getAttributes())
                .images(dto.getImages())
                .active(true)
                .rating(0.0)
                .reviewCount(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static ProductResponseDto toDto(Product product) {

        return ProductResponseDto.builder()
                .id(String.valueOf(product.getProduct_id()))
                .name(product.getName())
                .category(product.getCategory())
                .brand(product.getBrand())
                .price(product.getPrice())
                .discount(product.getDiscount())
                .attributes(product.getAttributes())
                .images(product.getImages())
                .active(product.isActive())
                .rating(product.getRating())
                .reviewCount(product.getReviewCount())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
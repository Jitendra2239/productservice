package com.jitendra.productservice.service;

import com.jitendra.productservice.dto.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface ProductService {

    ProductResponseDto createProduct(ProductRequestDto dto)throws ExecutionException, InterruptedException ;

    ProductResponseDto getProductById(Long id);

    List<ProductResponseDto> getAllProducts();

    List<ProductResponseDto> getByCategory(String category);

    List<ProductResponseDto> getByBrand(String brand);

    List<ProductResponseDto> searchByName(String name);

    List<ProductResponseDto> getTopRated();

    List<ProductResponseDto> getActiveProducts();

    ProductResponseDto updateProduct(Long id, ProductRequestDto dto);

    void deleteProduct(Long id);
}
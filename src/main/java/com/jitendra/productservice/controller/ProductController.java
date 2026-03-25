package com.jitendra.productservice.controller;

import com.jitendra.productservice.dto.ProductRequestDto;
import com.jitendra.productservice.dto.ProductResponseDto;
import com.jitendra.productservice.service.ProductService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(
             @RequestBody ProductRequestDto dto) throws ExecutionException, InterruptedException {

        ProductResponseDto response = productService.createProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(
            @PathVariable Long id) {

        return ResponseEntity.ok(productService.getProductById(id));
    }


    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {

        return ResponseEntity.ok(productService.getAllProducts());
    }


    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponseDto>> getByCategory(
            @PathVariable String category) {

        return ResponseEntity.ok(productService.getByCategory(category));
    }


    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<ProductResponseDto>> getByBrand(
            @PathVariable String brand) {

        return ResponseEntity.ok(productService.getByBrand(brand));
    }


    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDto>> searchByName(
            @RequestParam String name) {

        return ResponseEntity.ok(productService.searchByName(name));
    }


    @GetMapping("/top-rated")
    public ResponseEntity<List<ProductResponseDto>> getTopRated() {

        return ResponseEntity.ok(productService.getTopRated());
    }


    @GetMapping("/active")
    public ResponseEntity<List<ProductResponseDto>> getActiveProducts() {

        return ResponseEntity.ok(productService.getActiveProducts());
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable Long id,
           @RequestBody ProductRequestDto dto) {

        return ResponseEntity.ok(productService.updateProduct(id, dto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }
}
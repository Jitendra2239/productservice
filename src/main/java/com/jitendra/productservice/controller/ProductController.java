package com.jitendra.productservice.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.jitendra.productservice.dto.ProductRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jitendra.productservice.model.Product;
import com.jitendra.productservice.service.ProductService;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Create Product
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequestDto productDto) throws ExecutionException, InterruptedException {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setCategory(productDto.getCategory());
        product.setBrand(productDto.getBrand());
        product.setPrice(productDto.getPrice());
        product.setDiscount(productDto.getDiscount());
        product.setAttributes(productDto.getAttributes());
        product.setImages(productDto.getImages());
        product.setActive(productDto.isActive());

        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        product.setRating(0.0);
        product.setReviewCount(0);
        Product savedProduct = productService.createProduct(product);

        return ResponseEntity.ok(savedProduct);
    }

    // Update Product
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody Product product) {

        Product updatedProduct = productService.updateProduct(id, product);

        return ResponseEntity.ok(updatedProduct);
    }

    // Get Product By Id
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {

        return ResponseEntity.ok(productService.getProductById(id));
    }

    // Get All Products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {

        return ResponseEntity.ok(productService.getAllProducts());
    }

    // Search Products
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String name) {

        return ResponseEntity.ok(productService.searchProducts(name));
    }

    // Category Filter
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getByCategory(@PathVariable String category) {

        return ResponseEntity.ok(productService.getProductsByCategory(category));
    }

    // Brand Filter
    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<Product>> getByBrand(@PathVariable String brand) {

        return ResponseEntity.ok(productService.getProductsByBrand(brand));
    }

    // Price Filter
    @GetMapping("/price")
    public ResponseEntity<List<Product>> getByPriceRange(
            @RequestParam double min,
            @RequestParam double max) {

        return ResponseEntity.ok(productService.getProductsByPriceRange(min, max));
    }

    // Top Rated Products
    @GetMapping("/top-rated")
    public ResponseEntity<List<Product>> getTopRatedProducts() {

        return ResponseEntity.ok(productService.getTopRatedProducts());
    }

    // Deactivate Product
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deactivateProduct(@PathVariable Long id) {

        productService.deactivateProduct(id);

        return ResponseEntity.ok("Product deactivated successfully");
    }
}
//package com.jitendra.productservice.controller;
//
//import com.jitendra.productservice.dto.ProductRequestDTO;
//import com.jitendra.productservice.dto.ProductResponseDTO;
//import com.jitendra.productservice.model.Product;
//import com.jitendra.productservice.service.ProductService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/products")
//public class ProductController {
//  @Autowired
//    private ProductService productService;
//
////    public ProductController(ProductService productService) {
////        this.productService = productService;
////    }
//
//    @PostMapping
//    public ResponseEntity<ProductResponseDTO> createProduct(
//            @RequestBody ProductRequestDTO requestDTO) {
//       Product p=toEntity(requestDTO);
//       Product  response =
//                productService.createProduct(p);
//
//        return ResponseEntity.ok(toDTO(response));
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ProductResponseDTO> getProductById(
//            @PathVariable Long id) {
//
//        Product response =
//                productService.getProductById(id);
//
//        return ResponseEntity.ok(toDTO(response));
//    }
//
//    @GetMapping
//    public ResponseEntity<List<Product>> getAllProducts() {
//
//        List<Product> products =
//                productService.getAllProducts();
//
//        return ResponseEntity.ok(products);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<ProductResponseDTO> updateProduct(
//            @PathVariable Long id,
//            @RequestBody ProductRequestDTO requestDTO) {
//      Product p=toEntity(requestDTO);
//        Product response =
//                productService.updateProduct(id, p);
//
//        return ResponseEntity.ok(toDTO(response));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteProduct(
//            @PathVariable Long id) {
//
//        productService.deleteProduct(id);
//
//        return ResponseEntity.ok("Product deleted successfully");
//    }
//
//    public static Product toEntity(ProductRequestDTO dto) {
//
//        Product product = new Product();
//
//        product.setName(dto.getName());
//        product.setDescription(dto.getDescription());
//        product.setPrice(dto.getPrice());
//
//
//
//        product.setCreatedAt(LocalDateTime.now());
//        product.setUpdatedAt(LocalDateTime.now());
//
//        return product;
//    }
//    public static ProductResponseDTO toDTO(Product product) {
//
//        ProductResponseDTO dto = new ProductResponseDTO();
//
//
//        dto.setName(product.getName());
//        dto.setDescription(product.getDescription());
//        dto.setPrice(product.getPrice());
//
//
//
//
//        return dto;
//    }
//}
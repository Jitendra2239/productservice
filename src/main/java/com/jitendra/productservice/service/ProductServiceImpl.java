package com.jitendra.productservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import com.jitendra.event.*;
import com.jitendra.productservice.dto.ProductRequestDto;
import com.jitendra.productservice.dto.ProductResponseDto;
import com.jitendra.productservice.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;

import com.jitendra.productservice.exception.InvalidProductException;
import com.jitendra.productservice.exception.ProductAlreadyExistsException;
import com.jitendra.productservice.exception.ProductNotFoundException;
import com.jitendra.productservice.model.Product;
import com.jitendra.productservice.repository.ProductRepository;



import org.springframework.kafka.core.KafkaTemplate;



@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final KafkaTemplate<String, AddToCartResponseEvent> cartKafkaTemplate;
    private final KafkaTemplate<String, InventoryCheckEvent> inventoryKafkaTemplate;

  private final ReplyingKafkaTemplate<String, ProductCreatedEvent, InventoryCreatedEvent> kafkaTemplate;


    @Override
    public ProductResponseDto  createProduct(ProductRequestDto dto) throws ExecutionException, InterruptedException {
        Product product = ProductMapper.toEntity(dto);
        if(product.getPrice() <= 0) {
            throw new InvalidProductException("Product price must be greater than zero");
        }

        if(!productRepository.findByNameContainingIgnoreCase(product.getName()).isEmpty()) {
            throw new ProductAlreadyExistsException(
                    "Product already exists with name: " + product.getName());
        }

        product.setCreatedAt(LocalDateTime.now());
        product.setActive(true);
        product.setRating(0);
        product.setReviewCount(0);

        Product productResponse = productRepository.save(product);

        ProductCreatedEvent event = new ProductCreatedEvent();
        event.setProductId(productResponse.getProduct_id());
        event.setName(productResponse.getName());


            ProducerRecord<String, ProductCreatedEvent> record =
                    new ProducerRecord<>("product-created", event);

            RequestReplyFuture<String, ProductCreatedEvent, InventoryCreatedEvent> future =
                    kafkaTemplate.sendAndReceive(record);

        InventoryCreatedEvent response= future.get().value();


         return ProductMapper.toDto(productRepository.save(product));
    }

        @Override
        public ProductResponseDto getProductById(Long id) {

            Product product = productRepository.findById(id)
                    .orElseThrow(() ->
                            new ProductNotFoundException("Product not found: " + id));

            return ProductMapper.toDto(product);
        }

        @Override
        public List<ProductResponseDto> getAllProducts() {

            return productRepository.findAll()
                    .stream()
                    .map(ProductMapper::toDto)
                    .toList();
        }

        @Override
        public List<ProductResponseDto> getByCategory(String category) {

            return productRepository.findByCategory(category)
                    .stream()
                    .map(ProductMapper::toDto)
                    .toList();
        }

        @Override
        public List<ProductResponseDto> getByBrand(String brand) {

            return productRepository.findByBrand(brand)
                    .stream()
                    .map(ProductMapper::toDto)
                    .toList();
        }

        @Override
        public List<ProductResponseDto> searchByName(String name) {

            return productRepository.findByNameContainingIgnoreCase(name)
                    .stream()
                    .map(ProductMapper::toDto)
                    .toList();
        }

        @Override
        public List<ProductResponseDto> getTopRated() {

            return productRepository.findTop10ByOrderByRatingDesc()
                    .stream()
                    .map(ProductMapper::toDto)
                    .toList();
        }

        @Override
        public List<ProductResponseDto> getActiveProducts() {

            return productRepository.findByActiveTrue()
                    .stream()
                    .map(ProductMapper::toDto)
                    .toList();
        }

        @Override
        public ProductResponseDto updateProduct(Long id, ProductRequestDto dto) {

            Product product = productRepository.findById(id)
                    .orElseThrow(() ->
                            new ProductNotFoundException("Product not found: " + id));

            product.setName(dto.getName());
            product.setCategory(dto.getCategory());
            product.setBrand(dto.getBrand());
            product.setPrice(dto.getPrice());
            product.setDiscount(dto.getDiscount());
            product.setAttributes(dto.getAttributes());
            product.setImages(dto.getImages());
            product.setUpdatedAt(LocalDateTime.now());

            return ProductMapper.toDto(productRepository.save(product));
        }

        @Override
        public void deleteProduct(Long id) {

            Product product = productRepository.findById(id)
                    .orElseThrow(() ->
                            new ProductNotFoundException("Product not found: " + id));

            product.setActive(false); // soft delete
            productRepository.save(product);
        }

    @KafkaListener(topics = "add-to-cart", groupId = "product-group")
    public void consume(AddToCartEvent event) {

        Optional<Product> productOpt = productRepository.findById(event.getProductId());

        if (productOpt.isEmpty()) {
            AddToCartResponseEvent response = new AddToCartResponseEvent();
            response.setSuccess(false);
            response.setMessage("Product not found");
            cartKafkaTemplate.send("add-to-cart-response", response);
            return;
        }


        InventoryCheckEvent inventoryEvent = new InventoryCheckEvent();
        inventoryEvent.setProductId(event.getProductId());
        inventoryEvent.setQuantity(event.getQuantity());
        inventoryEvent.setUserId(event.getUserId());

        inventoryKafkaTemplate.send("inventory-check", inventoryEvent);
    }
    @KafkaListener(topics = "inventory-response", groupId = "product-group")
    public void handleInventoryResponse(AddToCartResponseEvent response) {

        if (!response.isSuccess()) {
            cartKafkaTemplate.send("add-to-cart-response", response);
            return;
        }

        // fetch product details again (for snapshot)
        Product product = productRepository.findById(response.getProductId()).get();

        response.setProductName(product.getName());
        response.setPrice(product.getPrice());

        cartKafkaTemplate.send("add-to-cart-response", response);
    }
}
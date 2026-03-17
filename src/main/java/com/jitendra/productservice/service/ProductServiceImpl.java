package com.jitendra.productservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import com.jitendra.event.*;
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
    public Product createProduct(Product product) throws ExecutionException, InterruptedException {

        if(product.getPrice() <= 0) {
            throw new InvalidProductException("Product price must be greater than zero");
        }

        if(productRepository.findByNameContainingIgnoreCase(product.getName()).size() > 0) {
            throw new ProductAlreadyExistsException(
                    "Product already exists with name: " + product.getName());
        }

        product.setCreatedAt(LocalDateTime.now());
        product.setActive(true);
        product.setRating(0);
        product.setReviewCount(0);

        Product productResponse = productRepository.save(product);

        ProductCreatedEvent event = new ProductCreatedEvent();
        event.setProductId(productResponse.getId());
        event.setName(productResponse.getName());


            ProducerRecord<String, ProductCreatedEvent> record =
                    new ProducerRecord<>("product-created", event);

            RequestReplyFuture<String, ProductCreatedEvent, InventoryCreatedEvent> future =
                    kafkaTemplate.sendAndReceive(record);

        InventoryCreatedEvent response= future.get().value();


        return productResponse;
    }

    @Override
    public Product updateProduct(Long id, Product product) {

        Product existing = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found with id: " + id));

        if(product.getPrice() <= 0) {
            throw new InvalidProductException("Invalid product price");
        }

        existing.setName(product.getName());
        existing.setCategory(product.getCategory());
        existing.setBrand(product.getBrand());
        existing.setPrice(product.getPrice());
        existing.setAttributes(product.getAttributes());

        return productRepository.save(existing);
    }

    @Override
    public Product getProductById(Long id) {

        return productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found with id: " + id));
    }

    @Override
    public List<Product> getAllProducts() {

        List<Product> products = productRepository.findByActiveTrue();

        if(products.isEmpty()) {
            throw new ProductNotFoundException("No products available");
        }

        return products;
    }

    @Override
    public List<Product> getProductsByCategory(String category) {

        List<Product> products = productRepository.findByCategory(category);

        if(products.isEmpty()) {
            throw new ProductNotFoundException(
                    "No products found for category: " + category);
        }

        return products;
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {

        List<Product> products = productRepository.findByBrand(brand);

        if(products.isEmpty()) {
            throw new ProductNotFoundException(
                    "No products found for brand: " + brand);
        }

        return products;
    }

    @Override
    public List<Product> searchProducts(String name) {

        List<Product> products = productRepository.findByNameContainingIgnoreCase(name);

        if(products.isEmpty()) {
            throw new ProductNotFoundException(
                    "No products found with name: " + name);
        }

        return products;
    }

    @Override
    public List<Product> getProductsByPriceRange(double min, double max) {

        if(min < 0 || max <= 0) {
            throw new InvalidProductException("Invalid price range");
        }

        List<Product> products = productRepository.findByPriceBetween(min, max);

        if(products.isEmpty()) {
            throw new ProductNotFoundException("No products found in price range");
        }

        return products;
    }

    @Override
    public List<Product> getTopRatedProducts() {

        List<Product> products = productRepository.findTop10ByOrderByRatingDesc();

        if(products.isEmpty()) {
            throw new ProductNotFoundException("No rated products available");
        }

        return products;
    }

    @Override
    public void deactivateProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found with id: " + id));

        product.setActive(false);

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

        // ✅ product exists → call Inventory via Kafka
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
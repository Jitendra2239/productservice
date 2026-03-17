package com.jitendra.productservice.config;

import com.jitendra.event.AddToCartResponseEvent;
import com.jitendra.event.InventoryCheckEvent;
import com.jitendra.event.InventoryCreatedEvent;
import com.jitendra.event.ProductCreatedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

@Configuration
public class KafkaConfig {
    @Bean
    public ConcurrentMessageListenerContainer<String, InventoryCreatedEvent>
    replyContainer(ConsumerFactory<String, InventoryCreatedEvent> consumerFactory) {

        ContainerProperties containerProperties =
                new ContainerProperties("inventory-created");

        return new ConcurrentMessageListenerContainer<>(consumerFactory, containerProperties);
    }
    @Bean
    public ReplyingKafkaTemplate<String, ProductCreatedEvent, InventoryCreatedEvent>
    replyingKafkaTemplate(
            ProducerFactory<String, ProductCreatedEvent> pf,
            ConcurrentMessageListenerContainer<String, InventoryCreatedEvent> container) {

        return new ReplyingKafkaTemplate<>(pf, container);
    }

    @Bean
    public KafkaTemplate<String, AddToCartResponseEvent> cartResponseKafkaTemplate(
            ProducerFactory<String, AddToCartResponseEvent> pf) {
        return new KafkaTemplate<>(pf);
    }

    @Bean
    public KafkaTemplate<String, InventoryCheckEvent> inventoryKafkaTemplate(
            ProducerFactory<String, InventoryCheckEvent> pf) {
        return new KafkaTemplate<>(pf);
    }
}


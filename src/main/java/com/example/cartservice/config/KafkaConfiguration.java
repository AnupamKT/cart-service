package com.example.cartservice.config;

import com.example.cartservice.model.Inventory;
import com.example.cartservice.model.Order;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {

    @Value("${kafka.bootstrap.server}")
    private String KAFKA_BOOTSTRAP_SERVER;
    @Value("${kafka.order.created.topic}")
    private String KAFKA_ORDER_TOPIC_NAME;
    @Value("${kafka.inventory.update.topic}")
    private String KAFKA_INVENTORY_TOPIC_NAME;
    @Value("${kafka.replication.factor}")
    private int KAFKA_REPLICATION_FACTOR;
    @Value("${kafka.partition.factor}")
    private int KAFKA_PARTITION_FACTOR;

    @Bean
    public Map<String,Object> getConfigProps(){
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BOOTSTRAP_SERVER);
        configMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return configMap;
    }

    @Bean
    public ProducerFactory<String, Order> getOrderProducerFactory() {
        return new DefaultKafkaProducerFactory<>(getConfigProps());
    }
    @Bean
    public ProducerFactory<String, Inventory> getInventoryProducerFactory() {
        return new DefaultKafkaProducerFactory<>(getConfigProps());
    }

    @Bean(name="orderKafkaTemplate")
    public KafkaTemplate getOrderKafkaTemplate() {
        return new KafkaTemplate(getOrderProducerFactory());
    }

    @Bean(name="inventoryKafkaTemplate")
    public KafkaTemplate getInventoryKafkaTemplate() {
        return new KafkaTemplate(getInventoryProducerFactory());
    }

    @Bean(name="orderTopic")
    public NewTopic orderTopic(){
        return new NewTopic(KAFKA_ORDER_TOPIC_NAME,
                KAFKA_PARTITION_FACTOR,
                (short) KAFKA_REPLICATION_FACTOR);
    }

    @Bean(name="inventoryTopic")
    public NewTopic inventoryTopic(){
        return new NewTopic(KAFKA_INVENTORY_TOPIC_NAME,
                KAFKA_PARTITION_FACTOR,
                (short) KAFKA_REPLICATION_FACTOR);
    }
}

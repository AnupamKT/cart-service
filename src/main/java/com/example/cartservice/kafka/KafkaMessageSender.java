package com.example.cartservice.kafka;

import com.example.cartservice.model.Inventory;
import com.example.cartservice.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaMessageSender {

    @Autowired
    @Qualifier("orderKafkaTemplate")
    private KafkaTemplate orderKafkaTemplate;
    @Autowired
    @Qualifier("inventoryKafkaTemplate")
    private KafkaTemplate inventoryKafkaTemplate;
    @Autowired
    @Qualifier("orderTopic")
    private NewTopic orderTopic;
    @Autowired
    @Qualifier("inventoryTopic")
    private NewTopic inventoryTopic;


    public void sendInventoryUpdateMessage(Inventory inventory) {
        try {
            inventoryKafkaTemplate.send(inventoryTopic.name(), inventory);
        } catch (Exception ex) {
            log.error("error occurred while sending kafka message for inventory update");
        }
    }

    public void sendOrderCreatedMessage(Order order) {
        try {
            orderKafkaTemplate.send(orderTopic.name(), order);
        } catch (Exception ex) {
            log.error("error occurred while sending kafka message for creating order");
        }
    }
}

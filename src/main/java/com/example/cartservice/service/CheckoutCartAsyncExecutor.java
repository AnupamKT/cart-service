package com.example.cartservice.service;

import com.example.cartservice.common.CartConstants;
import com.example.cartservice.common.CartServiceException;
import com.example.cartservice.converter.OrderConverter;
import com.example.cartservice.entity.CartDTO;
import com.example.cartservice.invoker.InventoryServiceInvoker;
import com.example.cartservice.invoker.OrderServiceInvoker;
import com.example.cartservice.kafka.KafkaMessageSender;
import com.example.cartservice.model.Inventory;
import com.example.cartservice.model.Order;
import com.example.cartservice.repository.CartRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class CheckoutCartAsyncExecutor {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrderConverter orderConverter;
    @Autowired
    private KafkaMessageSender kafkaSender;

    public void handleCheckoutCartAsyncOperation(List<CartDTO> cartDTOList) {
        CompletableFuture
                .runAsync(() -> invokeOrderService(cartDTOList))
                .thenRunAsync(() -> updateInventory(cartDTOList))
                .thenRunAsync(() -> deleteCartItems(cartDTOList));
    }

    private void deleteCartItems(List<CartDTO> cartDTOList) {
        cartRepository.deleteAll(cartDTOList);
    }

    private void updateInventory(List<CartDTO> cartDTOList) {
        ObjectMapper mapper = new ObjectMapper();
        for (CartDTO cartDTO : cartDTOList) {
            Inventory inventory = mapper.convertValue(cartDTO, Inventory.class);
            inventory.setAction(CartConstants.cartAction.DELETE.toString());
            kafkaSender.sendInventoryUpdateMessage(inventory);
        }
    }

    private void invokeOrderService(List<CartDTO> cartDTOList) {
        for (CartDTO cartDTO : cartDTOList) {
            Order order = orderConverter.prepareOrderServiceRequest(cartDTO);
            kafkaSender.sendOrderCreatedMessage(order);
        }
    }
}

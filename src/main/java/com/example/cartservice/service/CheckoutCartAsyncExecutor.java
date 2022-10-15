package com.example.cartservice.service;

import com.example.cartservice.common.CartConstants;
import com.example.cartservice.common.CartServiceException;
import com.example.cartservice.converter.OrderConverter;
import com.example.cartservice.entity.CartDTO;
import com.example.cartservice.invoker.InventoryServiceInvoker;
import com.example.cartservice.invoker.OrderServiceInvoker;
import com.example.cartservice.model.Inventory;
import com.example.cartservice.model.Order;
import com.example.cartservice.repository.CartRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class CheckoutCartAsyncExecutor {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private InventoryServiceInvoker inventoryServiceInvoker;
    @Autowired
    private OrderServiceInvoker orderServiceInvoker;
    @Autowired
    private OrderConverter orderConverter;

    @Autowired
    private ExecutorService executorService;

    public void handleCheckoutCartAsyncOperation(List<CartDTO> cartDTOList) {
        executorService.submit(() -> invokeOrderService(cartDTOList));
        executorService.submit(() -> updateInventory(cartDTOList));
        executorService.submit(() -> deleteCartItems(cartDTOList));
    }

    private void deleteCartItems(List<CartDTO> cartDTOList) {
        cartRepository.deleteAll(cartDTOList);
    }

    private void updateInventory(List<CartDTO> cartDTOList) {
        ObjectMapper mapper = new ObjectMapper();
        for (CartDTO cartDTO : cartDTOList) {
            Inventory inventory = mapper.convertValue(cartDTO, Inventory.class);
            inventory.setAction(CartConstants.cartAction.DELETE.toString());
            try {
                inventoryServiceInvoker.updateInventory(inventory);
            } catch (CartServiceException e) {
                log.error("error occurred while updating inventory", e);
            }
        }
    }

    private void invokeOrderService(List<CartDTO> cartDTOList) {
        for (CartDTO cartDTO : cartDTOList) {
            Order order = orderConverter.prepareOrderServiceRequest(cartDTO);
            try {
                orderServiceInvoker.createOrder(order);
            } catch (CartServiceException e) {
                String msg = "server error occurred while calling order service " + e.getMessage();
                log.error(msg);
            }
        }
    }


}

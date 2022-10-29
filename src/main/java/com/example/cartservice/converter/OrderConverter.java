package com.example.cartservice.converter;

import com.example.cartservice.entity.CartDTO;
import com.example.cartservice.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderConverter {


    public Order prepareOrderServiceRequest(CartDTO cartDTO) {
        Order order= new Order();
        order.setItemName(cartDTO.getProductName());
        order.setItemPrice((int)cartDTO.getPrice());
        order.setItemQuantity(cartDTO.getQuantity());
        order.setUserName(cartDTO.getUserName());
        return order;
    }
}

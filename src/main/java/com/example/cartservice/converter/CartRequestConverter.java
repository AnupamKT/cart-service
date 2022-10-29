package com.example.cartservice.converter;

import com.example.cartservice.entity.CartDTO;
import com.example.cartservice.model.CartRequest;
import org.springframework.stereotype.Component;

@Component
public class CartRequestConverter {
    public CartDTO convertCartRequest(CartRequest cartRequest, String userName) {
        CartDTO cartDTO= new CartDTO();
        cartDTO.setQuantity(cartRequest.getQuantity());
        cartDTO.setUserName(userName);
        cartDTO.setProductName(cartRequest.getProductName());
        cartDTO.setCategoryName(cartRequest.getCategoryName());
        cartDTO.setPrice(cartRequest.getPrice());
        return cartDTO;
    }
}

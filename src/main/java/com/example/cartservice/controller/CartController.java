package com.example.cartservice.controller;

import com.example.cartservice.common.CartServiceException;
import com.example.cartservice.common.InvalidCartRequestException;
import com.example.cartservice.model.CartRequest;
import com.example.cartservice.model.Response;
import com.example.cartservice.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<Response> addItemInCart(@RequestBody CartRequest cartRequest) throws Exception {
        Response response = cartService.addItemInCart(cartRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/checkout/{userId}")
    public ResponseEntity checkoutCart(@PathVariable String userId) throws Exception {
        Response response = cartService.checkoutCart(userId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}

package com.example.cartservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartRequest {
    private String userId;
    private UUID productId;
    private double price;
    private int quantity;
    private String productName;
    private String categoryName;
}

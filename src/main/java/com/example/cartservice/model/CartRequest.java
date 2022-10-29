package com.example.cartservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartRequest {
    private UUID productId;
    private int quantity;
    private String productName;
    private String categoryName;
    private double price;
    @JsonIgnore
    private int inventoryQuantity;
}

package com.example.cartservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private String itemName;
    private int itemPrice;
    private int itemQuantity;
    private Long userId;
}

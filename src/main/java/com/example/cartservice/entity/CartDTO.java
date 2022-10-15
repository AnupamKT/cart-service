package com.example.cartservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="CART_INFO"
        ,uniqueConstraints = {@UniqueConstraint(name="uniqueUserIdAndProductName"
        ,columnNames ={"userId","productName"})})
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID cartId;
    private String userId;
    private String productName;
    private double price;
    private int quantity;
    private String categoryName;
}

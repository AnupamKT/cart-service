package com.example.cartservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDetails {
    private String customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private String userName;
}

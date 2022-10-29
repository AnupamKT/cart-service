package com.example.cartservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerServiceProperties {
    private String serviceName;
    private String userName;
    private String password;
}

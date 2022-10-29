package com.example.cartservice.config;

import com.example.cartservice.model.CustomerServiceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;


@Configuration
public class CartServiceConfiguration {

    @Autowired
    private Environment environment;
    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate(RestTemplateBuilder restTemplateBuilder){
      RestTemplate restTemplate = restTemplateBuilder
                .setReadTimeout(Duration.ofMillis(3000))
                .setConnectTimeout(Duration.ofMillis(3000)).build();
      return restTemplate;
    }

    @Bean
    public CustomerServiceProperties createCustomerServiceProps(){
        CustomerServiceProperties props= new CustomerServiceProperties();
        props.setServiceName(environment.getProperty("customer.service.name"));
        props.setUserName(environment.getProperty("customer.service.username"));
        props.setPassword(environment.getProperty("customer.service.password"));
        return props;
    }
}

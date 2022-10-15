package com.example.cartservice.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class CartServiceConfiguration {

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate(RestTemplateBuilder restTemplateBuilder){
      RestTemplate restTemplate = restTemplateBuilder
                .setReadTimeout(Duration.ofMillis(3000))
                .setConnectTimeout(Duration.ofMillis(3000)).build();
      return restTemplate;
    }

    @Bean
    public ExecutorService getExecutorService(){
        return Executors.newFixedThreadPool(5);
    }
}

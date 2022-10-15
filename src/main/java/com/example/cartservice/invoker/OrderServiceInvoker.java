package com.example.cartservice.invoker;

import com.example.cartservice.common.CartServiceException;
import com.example.cartservice.entity.CartDTO;
import com.example.cartservice.model.Order;
import com.example.cartservice.model.ProductResponse;
import com.example.cartservice.model.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static com.example.cartservice.common.CartConstants.HTTP_STRING;

@Component
@Slf4j
public class OrderServiceInvoker {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${order.service.name}")
    private String orderServiceName;

    public void createOrder(Order order) throws CartServiceException {
        String url = HTTP_STRING + orderServiceName + "/order/save";
        try {
            log.info("invoking order service for creating order");
            ResponseEntity<Response> response = restTemplate.postForEntity(url, order, Response.class);
            handleOrderServiceResponse(response);
        } catch (Exception e) {
            String msg = "server error occurred while calling order service " + e.getMessage();
            throw new CartServiceException(msg);
        }
    }

    private void handleOrderServiceResponse(ResponseEntity<Response> response) throws CartServiceException {
        if (response != null && response.getStatusCodeValue() == 200) {
            if (response.hasBody()) {
                log.info("order created successfully");
            } else {
                String msg = "error response received while invoking order service";
                log.error(msg);
                throw new CartServiceException(msg);
            }
        } else {
            String msg = "error occurred while invoking order service";
            log.error(msg);
            throw new CartServiceException(msg);
        }
    }
}

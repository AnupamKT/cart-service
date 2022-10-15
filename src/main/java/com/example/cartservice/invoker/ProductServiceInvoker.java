package com.example.cartservice.invoker;

import com.example.cartservice.common.CartServiceException;
import com.example.cartservice.model.ProductResponse;
import com.example.cartservice.model.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static com.example.cartservice.common.CartConstants.HTTP_STRING;

@Component
public class ProductServiceInvoker {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${product.service.name}")
    private String ProductServiceName;

    public ProductResponse getProductDetails(UUID productId) throws CartServiceException {
        String url = HTTP_STRING + ProductServiceName + "/product/" + productId;
        ProductResponse productResponse = null;
        try {
            ResponseEntity<Response> response = restTemplate.getForEntity(url, Response.class);
            productResponse = processProductServiceResponse(response);
        } catch (Exception e) {
            String msg = "product details not found!!" + e.getMessage();
            throw new CartServiceException(msg);
        }
        return productResponse;
    }

    private ProductResponse processProductServiceResponse(ResponseEntity<Response> response) throws CartServiceException {
        if (response != null && response.getStatusCodeValue() == 200) {
            if (response.hasBody()) {
                ObjectMapper mapper = new ObjectMapper();
                ProductResponse productResponse = mapper.
                        convertValue(response.getBody().getBody(), ProductResponse.class);
                return productResponse;

            } else {
                String msg = "error response received while invoking product service";
                throw new CartServiceException(msg);
            }
        } else {
            String msg = "error occurred while invoking product service";
            throw new CartServiceException(msg);
        }
    }
}

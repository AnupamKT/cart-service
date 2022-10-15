package com.example.cartservice.invoker;

import com.example.cartservice.common.CartServiceException;
import com.example.cartservice.model.Inventory;
import com.example.cartservice.model.InventoryDetailsResponse;
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
public class InventoryServiceInvoker {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${inventory.service.name}")
    private String inventoryServiceName;

    public InventoryDetailsResponse getInventoryDetails(String productName) throws CartServiceException {
        String url = HTTP_STRING + inventoryServiceName + "/inventory/" + productName;
        InventoryDetailsResponse inventoryResponse = null;
        try {
            ResponseEntity<Response> response = restTemplate.getForEntity(url, Response.class);
            inventoryResponse = processResponse(response);
        } catch (Exception e) {
            String msg = "server error occurred while calling inventory service " + e.getMessage();
            throw new CartServiceException(msg);
        }
        return inventoryResponse;
    }

    public void updateInventory(Inventory inventory) throws CartServiceException {
        String url = HTTP_STRING + inventoryServiceName + "/inventory/update";
        try {
            ResponseEntity<Response> response = restTemplate.postForEntity(url, inventory, Response.class);
            processUpdateInventoryResponse(response);

        } catch (Exception e) {
            String msg = "server error occurred while updating inventory " + e.getMessage();
            throw new CartServiceException(msg);
        }
    }

    private void processUpdateInventoryResponse(ResponseEntity<Response> response) throws CartServiceException {
        if (response != null && response.hasBody()) {
            if (response.getStatusCodeValue() == 200) {
                log.info("inventory updated successfully");
            } else {
                String errorMsg = response.getBody().getBody().toString();
                String msg = "error response received while invoking inventory service: " + errorMsg;
                throw new CartServiceException(msg);
            }
        } else {
            String msg = "error occurred while invoking inventory service";
            throw new CartServiceException(msg);
        }
    }

    private InventoryDetailsResponse processResponse(ResponseEntity<Response> response) throws CartServiceException {
        if (response != null && response.getStatusCodeValue() == 200) {
            if (response.hasBody()) {
                ObjectMapper mapper = new ObjectMapper();
                InventoryDetailsResponse inventoryDetailsResponse = mapper.
                        convertValue(response.getBody().getBody(), InventoryDetailsResponse.class);
                return inventoryDetailsResponse;
            } else {
                String msg = "error response received while invoking inventory service";
                throw new CartServiceException(msg);
            }
        } else {
            String msg = "error occurred while invoking inventory service";
            throw new CartServiceException(msg);
        }
    }
}

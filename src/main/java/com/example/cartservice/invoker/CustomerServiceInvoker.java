package com.example.cartservice.invoker;

import com.example.cartservice.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static com.example.cartservice.common.CartConstants.HTTP_STRING;

@Component
@Slf4j
public class CustomerServiceInvoker {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CustomerServiceProperties props;

    public String getToken() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUserName(props.getUserName());
        request.setPassword(props.getPassword());
        String customerServiceName = props.getServiceName();
        String token = null;
        try {
            String url = HTTP_STRING + customerServiceName + "/authenticate";
            ResponseEntity<AuthenticationResponse> response = restTemplate
                    .postForEntity(url, request, AuthenticationResponse.class);
            token = processAuthenticationResponse(response);
        } catch (Exception ex) {
            log.error("error occurred while authenticating cart service with user service");
        }
        return "Bearer " + token;
    }

    private String processAuthenticationResponse(ResponseEntity<AuthenticationResponse> response) {
        String token = null;
        if (response != null && response.hasBody()) {
            if (response.getStatusCodeValue() == 200) {
                token = response.getBody().getJwtToken();
            } else {
                //throw exception
                log.error("error occurred while authenticating cart service with user service");
            }
        } else {
            //throw exception
            log.error("error occurred while authenticating cart service with user service");
        }
        return token;
    }

    public CustomerDetails getCustomerDetails(String userName) {
        CustomerDetails customerDetails = null;
        String customerServiceName = props.getServiceName();
        String url = HTTP_STRING + customerServiceName + "/customer/" + userName;
        HttpHeaders header = new HttpHeaders();
        header.set("Authorization", getToken());
        HttpEntity entity = new HttpEntity(header);

        try {
            ResponseEntity<Response> response = restTemplate.
                    exchange(url, HttpMethod.GET, entity, Response.class);
            log.debug("response received from get customer call " + response);
            customerDetails = processCustomerDetails(response);
        } catch (Exception ex) {
            String msg = "error occurred while invoking user service for getting customer details for username" + userName;
            log.error(msg);
        }
        return customerDetails;
    }

    private CustomerDetails processCustomerDetails(ResponseEntity<Response> response) {
        CustomerDetails details = null;
        if (response != null && response.hasBody()) {
            if (response.getStatusCodeValue() == 200) {
                ObjectMapper mapper = new ObjectMapper();
                details = mapper.
                        convertValue(response.getBody().getBody(), CustomerDetails.class);
            } else {
                //throw exception
                log.error("error occurred while processing user details response");
            }
        } else {
            //throw exception
            log.error("error occurred while processing user details response");
        }
        return details;
    }
}

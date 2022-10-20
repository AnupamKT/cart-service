package com.example.cartservice.validator;

import com.example.cartservice.common.CartServiceException;
import com.example.cartservice.common.InvalidCartRequestException;
import com.example.cartservice.invoker.InventoryServiceInvoker;
import com.example.cartservice.invoker.ProductServiceInvoker;
import com.example.cartservice.model.CartRequest;
import com.example.cartservice.model.InventoryDetailsResponse;
import com.example.cartservice.model.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddCartValidatorImpl implements CartValidatorIF {

    @Autowired
    private ProductServiceInvoker productServiceInvoker;

    @Autowired
    private InventoryServiceInvoker inventoryServiceInvoker;


    /**
     * get productName by calling product service
     * check inventory to see if required quantity is available
     */
    @Override
    public void validate(Object obj) throws Exception {
        if (obj instanceof CartRequest) {
            CartRequest cartRequest = (CartRequest) obj;
            //validate  cart request
            validatePriceAndQuantity(cartRequest);
            //get productName by productId
            populateProductName(cartRequest);
            //validate with Inventory
            validateQuantityInInventory(cartRequest);
        }
    }

    private void validateQuantityInInventory(CartRequest cartRequest) throws CartServiceException, InvalidCartRequestException {


        InventoryDetailsResponse inventoryDetails = inventoryServiceInvoker.
                getInventoryDetails(cartRequest.getProductName());

        //throw exception when inventory Quantity is less than ordered quantity
        int OrderedQuantity = cartRequest.getQuantity();
        int inventoryQuantity = inventoryDetails.getQuantity().intValue();
        if (OrderedQuantity > inventoryQuantity) {
            String msg = "Ordered Quantity is more than available quantity";
            throw new InvalidCartRequestException(msg);
        }else{
            cartRequest.setInventoryQuantity(inventoryQuantity);
        }
    }

    private void populateProductName(CartRequest cartRequest) throws CartServiceException {
        ProductResponse response = productServiceInvoker.getProductDetails(cartRequest.getProductId());
        cartRequest.setProductName(response.getProductName());
        cartRequest.setCategoryName(response.getCategoryName());
        cartRequest.setPrice(response.getPrice());
    }

    private void validatePriceAndQuantity(CartRequest cartRequest) throws InvalidCartRequestException {
        if (cartRequest.getQuantity() <= 0 || cartRequest.getPrice() < 0) {
            String msg = "Invalid Request!! price and quantity must be greater than zero";
            throw new InvalidCartRequestException(msg);
        }
    }
}

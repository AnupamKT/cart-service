package com.example.cartservice.validator;

import com.example.cartservice.common.CartServiceException;
import com.example.cartservice.common.InvalidCartRequestException;
import com.example.cartservice.entity.CartDTO;
import com.example.cartservice.invoker.InventoryServiceInvoker;
import com.example.cartservice.model.InventoryDetailsResponse;
import com.example.cartservice.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.CompletableFuture;
@Component
public class CheckoutCartValidatorImpl implements CartValidatorIF {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private InventoryServiceInvoker inventoryServiceInvoker;

    @Override
    public void validate(Object obj) throws Exception {
        if (obj instanceof List) {
            List<CartDTO> cartDTOList = (List<CartDTO>) obj;
            for (CartDTO cartDTO : cartDTOList) {
                validateWithInventory(cartDTO);
            }
        }
    }

    private void validateWithInventory(CartDTO cartDTO) throws InvalidCartRequestException {
        InventoryDetailsResponse inventoryDetailsResponse = null;
        try {
            inventoryDetailsResponse = inventoryServiceInvoker
                    .getInventoryDetails(cartDTO.getProductName());
        } catch (CartServiceException e) {
            throw new InvalidCartRequestException(e.getMessage());
        }
        int cartQuantity = cartDTO.getQuantity();
        int inventoryQuantity = inventoryDetailsResponse.getQuantity().intValue();
        if (cartQuantity > inventoryQuantity) {
            String msg = "product " + cartDTO.getProductName() + "not available in inventory";
            throw new InvalidCartRequestException(msg);
        }
    }
}

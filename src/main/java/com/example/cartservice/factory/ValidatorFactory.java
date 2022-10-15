package com.example.cartservice.factory;

import com.example.cartservice.common.CartConstants;
import com.example.cartservice.validator.AddCartValidatorImpl;
import com.example.cartservice.validator.CartValidatorIF;
import com.example.cartservice.validator.CheckoutCartValidatorImpl;
import com.example.cartservice.validator.DeleteCartValidatorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidatorFactory {

    @Autowired
    private AddCartValidatorImpl addCartValidator;
    @Autowired
    private CheckoutCartValidatorImpl checkoutCartValidator;
    @Autowired
    private DeleteCartValidatorImpl deleteCartValidator;

    public CartValidatorIF getCartValidator(String action) {
        CartValidatorIF cartValidatorIF = null;
        if (CartConstants.cartAction.ADD.toString().equalsIgnoreCase(action)) {
            cartValidatorIF = addCartValidator;
        } else if (CartConstants.cartAction.CHECKOUT.toString().equalsIgnoreCase(action)) {
            cartValidatorIF = checkoutCartValidator;
        } else if (CartConstants.cartAction.DELETE.toString().equalsIgnoreCase(action)) {
            cartValidatorIF = deleteCartValidator;
        }
        return cartValidatorIF;
    }
}

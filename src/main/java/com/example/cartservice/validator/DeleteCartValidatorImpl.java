package com.example.cartservice.validator;

import com.example.cartservice.common.InvalidCartRequestException;
import com.example.cartservice.model.CartRequest;
import org.springframework.stereotype.Component;

@Component
public class DeleteCartValidatorImpl implements CartValidatorIF{
    @Override
    public void validate(Object obj) throws Exception {

    }
}

package com.example.cartservice.service;

import com.example.cartservice.common.CartConstants;
import com.example.cartservice.common.CartServiceException;
import com.example.cartservice.common.InvalidCartRequestException;
import com.example.cartservice.converter.CartRequestConverter;
import com.example.cartservice.entity.CartDTO;
import com.example.cartservice.factory.ValidatorFactory;
import com.example.cartservice.model.CartRequest;
import com.example.cartservice.model.Response;
import com.example.cartservice.repository.CartRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private ValidatorFactory validatorFactory;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CheckoutCartAsyncExecutor executor;
    @Autowired
    private CartRequestConverter converter;


    /**
     * Validate the request
     * if product is already there in user cart, then just increase the quantity of cart item
     * otherwise add in cart
     *
     * @return Response-- response
     */


    public Response addItemInCart(CartRequest cartRequest, String userName) throws Exception {
        Response response = null;
        validatorFactory
                .getCartValidator(CartConstants.cartAction.ADD.toString())
                .validate(cartRequest);
        CartDTO cartDTO = processAddItemInCart(cartRequest,userName);
        if (cartDTO != null) {
            response = new Response(200, cartDTO);
        } else {
            String msg = "server error occurred while adding item to cart";
            throw new CartServiceException(msg);
        }
        return response;
    }

    private CartDTO processAddItemInCart(CartRequest cartRequest,String userName) throws InvalidCartRequestException {
        CartDTO cartDTO = null;
        Optional<CartDTO> optionalCartDTO = Optional.empty();
        optionalCartDTO = cartRepository
                    .findByUserNameAndProductName(userName, cartRequest.getProductName());

        if (optionalCartDTO.isPresent()) {
            //if present then just increase the cart quantity
            cartDTO = optionalCartDTO.get();
            int newQuantity = cartDTO.getQuantity() + cartRequest.getQuantity();
            if (newQuantity > cartRequest.getInventoryQuantity()) {
                String msg = "Ordered Quantity is more than available quantity";
                throw new InvalidCartRequestException(msg);
            } else {
                cartDTO.setQuantity(newQuantity);
            }
        } else {

            cartDTO = converter.convertCartRequest(cartRequest,userName);
        }
        return cartRepository.save(cartDTO);
    }

    /**
     * validate if anything is there in cart
     * validate for each cart entry with inventory
     * if inventory check is true
     * call order service async to create order request
     * delete cart entries
     */
    public Response checkoutCart(String userName) throws Exception {
        List<CartDTO> cartDTOList = cartRepository.findByUserName(userName);

        if (!CollectionUtils.isEmpty(cartDTOList)) {
            validatorFactory
                    .getCartValidator(CartConstants.cartAction.CHECKOUT.toString())
                    .validate(cartDTOList);
            executor.handleCheckoutCartAsyncOperation(cartDTOList);
        }
        String msg = "Order created successfully";
        return new Response(202, msg);
    }
}

package com.example.cartservice.service;

import com.example.cartservice.entity.CartDTO;
import com.example.cartservice.invoker.InventoryServiceInvoker;
import com.example.cartservice.model.InventoryDetailsResponse;
import com.example.cartservice.repository.CartRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CartSchedulerService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private InventoryServiceInvoker invoker;

    public void checkCartEntriesWithInventory() {
        List<CartDTO> cartDTOList = cartRepository.findAll();
        //cart entries grouped by userId
        Map<String, List<CartDTO>> cartEntriesByUserId = cartDTOList
                .stream()
                .collect(Collectors.groupingBy(CartDTO::getUserId));

        cartEntriesByUserId
                .entrySet()
                .forEach(entry -> checkUserCartEntryWithInventory(entry.getValue(), entry.getKey()));
    }

    private void checkUserCartEntryWithInventory(List<CartDTO> cartDTOList, String userId) {
        List<CartDTO> invalidCartEntries = cartDTOList
                .stream()
                .filter(this::getInventoryDetails)
                .collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(invalidCartEntries)) {
            log.debug("found invalid cart entries {} for userId {} ",invalidCartEntries,userId);
            cartRepository.deleteAll(invalidCartEntries);
            //send notification to user using userId
        }
    }

    /**
     * This method gets inventory details for each product
     * compare inventory quantity with cart quantity
     * return true if cart quantity is more than inventory quantity
     */
    private boolean getInventoryDetails(CartDTO cartDTO) {
        InventoryDetailsResponse inventoryDetailsResponse = null;
        try {
            inventoryDetailsResponse = invoker.getInventoryDetails(cartDTO.getProductName());
        } catch (Exception e) {
            log.error("error occurred while fetching inventory details for product: " + cartDTO.getProductName());
        }
        return cartDTO.getQuantity() > inventoryDetailsResponse.getQuantity();
    }
}

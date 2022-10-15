package com.example.cartservice.repository;

import com.example.cartservice.entity.CartDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<CartDTO, UUID> {

    Optional<CartDTO> findByUserIdAndProductName(String userId, String productName);
    List<CartDTO> findByUserId(String userId);
}

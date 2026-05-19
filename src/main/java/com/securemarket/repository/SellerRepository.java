package com.securemarket.repository;

import com.securemarket.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SellerRepository extends JpaRepository<Seller, UUID> {
    // Só de herdar o JpaRepository, você já ganhou o .save(), .findAll(), .findById() de graça!
}
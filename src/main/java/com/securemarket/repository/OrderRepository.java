package com.securemarket.repository;

import com.securemarket.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    // Busca todos os pedidos onde o ID do cliente correspondente seja igual ao passado por parâmetro
    List<Order> findByCustomerId(UUID customerId);
}
package com.securemarket.controller;

import com.securemarket.dto.OrderRequestDTO;
import com.securemarket.entity.Order;
import com.securemarket.entity.User;
import com.securemarket.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders") // Rota: http://localhost:8080/orders
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody @Valid OrderRequestDTO requestDTO) {
        // 1. Pega o cliente logado via Token JWT (Pulseira VIP)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = (User) authentication.getPrincipal();

        // 2. Cria o pedido passando o ID do cliente e o carrinho de compras
        Order newOrder = orderService.createOrder(loggedInUser.getId(), requestDTO);

        // 3. Devolve o pedido gerado com Status 201 (Created)
        return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
    }
    @GetMapping
    public ResponseEntity<List<Order>> getMyOrders() {
        // 1. Pega o usuário logado direto do Token JWT
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = (User) authentication.getPrincipal();

        // 2. Busca a lista de pedidos dele
        List<Order> orders = orderService.getOrdersByCustomer(loggedInUser.getId());

        // 3. Retorna a lista com status 200 OK
        return ResponseEntity.ok(orders);
    }
}
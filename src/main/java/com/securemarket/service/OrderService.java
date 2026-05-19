package com.securemarket.service;

import com.securemarket.dto.OrderItemRequestDTO;
import com.securemarket.dto.OrderRequestDTO;
import com.securemarket.entity.Order;
import com.securemarket.entity.OrderItem;
import com.securemarket.entity.Product;
import com.securemarket.entity.User;
import com.securemarket.repository.OrderItemRepository;
import com.securemarket.repository.OrderRepository;
import com.securemarket.repository.ProductRepository;
import com.securemarket.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                        ProductRepository productRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional // 🔴 ESSENCIAL: Se faltar estoque em um item, o Spring desfaz TUDO no banco (Rollback)
    public Order createOrder(UUID customerId, OrderRequestDTO dto) {
        // 1. Busca o cliente que está comprando
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        // 2. Instancia o pedido inicial (começa com valor zero)
        Order order = Order.builder()
                .customer(customer)
                .status("PENDENTE")
                .totalAmount(BigDecimal.ZERO)
                .build();

        order = orderRepository.save(order);

        BigDecimal totalOrderAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        // 3. Processa cada item do carrinho de compras
        for (OrderItemRequestDTO itemDTO : dto.items()) {
            Product product = productRepository.findById(itemDTO.productId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + itemDTO.productId()));

            // Regra de Negócio: Tem estoque disponível?
            if (product.getStockQuantity() < itemDTO.quantity()) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + product.getName());
            }

            // Atualiza o estoque do produto (Dá baixa)
            product.setStockQuantity(product.getStockQuantity() - itemDTO.quantity());
            productRepository.save(product);

            // Calcula o valor total deste item (Preço unitário x Quantidade)
            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(itemDTO.quantity()));
            totalOrderAmount = totalOrderAmount.add(itemTotal);

            // Monta o item do pedido
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemDTO.quantity())
                    .unitPrice(product.getPrice()) // Trava o preço histórico da compra
                    .build();

            orderItems.add(orderItem);
        }

        // 4. Salva todos os itens do carrinho de uma vez só
        orderItemRepository.saveAll(orderItems);

        // 5. Atualiza o valor final do Pedido e salva
        order.setTotalAmount(totalOrderAmount);
        return orderRepository.save(order);
    }
    public List<Order> getOrdersByCustomer(UUID customerId) {
        return orderRepository.findByCustomerId(customerId);
    }
}
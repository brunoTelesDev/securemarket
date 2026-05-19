package com.securemarket.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order; // O pedido pai ao qual este item pertence

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; // O produto que foi comprado

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice; // Salvamos o preço do momento da compra (se o vendedor mudar o preço depois, o histórico não quebra!)
}
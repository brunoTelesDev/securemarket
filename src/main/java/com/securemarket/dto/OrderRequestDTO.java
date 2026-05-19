package com.securemarket.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record OrderRequestDTO(
        @NotEmpty(message = "O carrinho não pode estar vazio")
        @Valid
        List<OrderItemRequestDTO> items
) {}
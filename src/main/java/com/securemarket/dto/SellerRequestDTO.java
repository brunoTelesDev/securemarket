package com.securemarket.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SellerRequestDTO(
        @NotBlank(message = "O CNPJ é obrigatório")
        @Size(min = 14, max = 18, message = "CNPJ inválido")
        String cnpj,

        @NotBlank(message = "O nome da loja é obrigatório")
        String storeName,

        String description
) {}
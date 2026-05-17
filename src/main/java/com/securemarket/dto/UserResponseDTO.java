package com.securemarket.dto;

import java.util.UUID;

// Essa é que vai para o cliente. Não tem senha aqui!
public record UserResponseDTO(
        UUID id,
        String name,
        String email,
        String role
) {
}
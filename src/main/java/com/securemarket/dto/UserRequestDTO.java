package com.securemarket.dto;
import com.securemarket.entity.UserRole;
// A COMANDA DO CLIENTE (O que o Front-end envia
public record UserRequestDTO(
        String name,
        String email,
        String password,
        UserRole role
) {
    // Fica 100% vazio aqui dentro!
    // O Front-end só tem o direito de mandar essas 4 informações.
}
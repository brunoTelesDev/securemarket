package com.securemarket.entity;

// O 'enum' é o nosso "menu restrito". Ele substitui o 'class'.
// Agora, o sistema só reconhece essas 3 palavras exatas. Qualquer erro de digitação será bloqueado.
public enum UserRole {
    ADMIN,    // Dono do marketplace / Administrador do sistema
    SELLER,   // Vendedor (Dono de uma loja)
    CUSTOMER  // Cliente padrão que faz compras
}
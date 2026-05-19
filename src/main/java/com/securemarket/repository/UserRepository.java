package com.securemarket.repository;

import com.securemarket.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface UserRepository extends JpaRepository<User, UUID> {
    // findBy = Busque por
    // Name = Coluna 'name'
    // Containing = Que contenha esse pedaço de texto (SQL LIKE '%texto%')
    // IgnoreCase = Ignorando maiúsculas e minúsculas
    List<User> findByNameContainingIgnoreCase(String name);
    // Método para o Spring buscar o usuário pelo e-mail exato
    Optional<User> findByEmail(String email);

}
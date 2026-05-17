package com.securemarket.repository;

// Importamos a nossa "planta da tabela" (A entidade User)
import com.securemarket.entity.User;
// Importamos a ferramenta mágica do Spring que já tem os comandos SQL prontos
import org.springframework.data.jpa.repository.JpaRepository;
// Importamos o tipo de dado que usamos no nosso ID
import java.util.UUID;


// A INTERFACE DE COMUNICAÇÃO

public interface UserRepository extends JpaRepository<User, UUID> {

}
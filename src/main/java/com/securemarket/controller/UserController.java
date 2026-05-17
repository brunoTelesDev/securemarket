package com.securemarket.controller;

// importar os dois DTOs
import com.securemarket.dto.UserRequestDTO;
import com.securemarket.dto.UserResponseDTO;
import com.securemarket.entity.User;
import com.securemarket.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    // O controle remoto do banco de dados
    private final UserRepository userRepository;

    // Injeção de dependência (O Spring entrega o repository pronto)
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ==========================================
    // ROTA DE CADASTRAR USUÁRIO (POST)
    // ==========================================
    @PostMapping

    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO requestDTO) {

        // PASSO 1: Receber a comanda e transformar na Entidade (Cozinha)
        User novoUsuario = User.builder()
                .name(requestDTO.name())
                .email(requestDTO.email())
                .password(requestDTO.password())
                .role(requestDTO.role())
                .build();

        // PASSO 2: Salvar no banco de dados
        User usuarioSalvo = userRepository.save(novoUsuario);

        // PASSO 3: Empratar a resposta segura (Tirando a senha)
        UserResponseDTO resposta = new UserResponseDTO(
                usuarioSalvo.getId(),
                usuarioSalvo.getName(),
                usuarioSalvo.getEmail(),
                usuarioSalvo.getRole().name()
        );

        // PASSO 4: Devolver para o Front-end
        return ResponseEntity.ok(resposta);
    }
}
package com.securemarket.controller; // ou com.bruno.securemarket.controller

import com.securemarket.dto.UserRequestDTO;
import com.securemarket.dto.UserResponseDTO;
import com.securemarket.entity.User;
import com.securemarket.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder; // IMPORT DO TRITURADOR
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Declarando o Triturador

    // ✅ NOVO: Agora o Spring entrega o banco e o triturador prontos na mão do garçom
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO requestDTO) {

        // ✅ NOVO: Triturando a senha que veio do Postman
        String senhaCriptografada = passwordEncoder.encode(requestDTO.password());

        // PASSO 1: Receber a comanda e transformar na Entidade
        User novoUsuario = User.builder()
                .name(requestDTO.name())
                .email(requestDTO.email())
                .password(senhaCriptografada) // Mandar senha embaralhada pro banco!
                .role(requestDTO.role())
                .build();

        // PASSO 2: Salvar no banco de dados
        User usuarioSalvo = userRepository.save(novoUsuario);

        // PASSO 3: Empratar a resposta segura
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
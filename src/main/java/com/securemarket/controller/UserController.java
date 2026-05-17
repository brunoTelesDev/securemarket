package com.securemarket.controller;

import com.securemarket.dto.UserRequestDTO;
import com.securemarket.dto.UserResponseDTO;
import com.securemarket.entity.User;
import com.securemarket.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder; // IMPORT DO TRITURADOR
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Declarando o Triturador

    // Spring entrega o banco e o triturador prontos na mão do garçom
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO requestDTO) {

        // Triturando a senha que veio do Postman
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
    // ==========================================
    // ROTA DE LISTAR TODOS OS USUÁRIOS (GET)
    // ==========================================
    @GetMapping // Avisa que esse método responde a requisições de LEITURA
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {

        // 1. O garçom vai no banco e pega TODOS os usuários (com senha e tudo)
        List<User> usuariosDoBanco = userRepository.findAll();

        // 2. A "Esteira de Fábrica": Transformamos cada User(Sujo) em UserResponseDTO(Limpo)
        List<UserResponseDTO> usuariosLimpos = usuariosDoBanco.stream()
                .map(user -> new UserResponseDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole().name()
                ))
                .toList();

        // 3. Devolvo a lista limpa para o cliente
        return ResponseEntity.ok(usuariosLimpos);
    }
    // ==========================================
    // ROTA DE BUSCAR UM USUÁRIO ESPECÍFICO POR ID
    // ==========================================
    @GetMapping("/{id}") // O link vai ficar: http://localhost:8080/users/NUMERO-DO-ID
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id) {

        // 1. O garçom vai no banco procurar.
        // Como o usuário pode não existir, usei o .orElseThrow para estourar um erro caso não ache.
        User usuarioDoBanco = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        // 2. Empratar o resultado na bandeja segura (DTO)
        UserResponseDTO resposta = new UserResponseDTO(
                usuarioDoBanco.getId(),
                usuarioDoBanco.getName(),
                usuarioDoBanco.getEmail(),
                usuarioDoBanco.getRole().name()
        );

        // 3. Devolver para o cliente
        return ResponseEntity.ok(resposta);
    }
    // ==========================================
    // ROTA DE PESQUISA POR NOME (GET)
    // ==========================================
    @GetMapping("/search") // O link vai ficar: http://localhost:8080/users/search?name=TEXTO
    public ResponseEntity<List<UserResponseDTO>> searchUsersByName(@RequestParam String name) {

        // 1. O garçom pede pro banco procurar a lista de pessoas com esse pedaço de nome
        List<User> usuariosDoBanco = userRepository.findByNameContainingIgnoreCase(name);

        // 2. A "Esteira de Fábrica": Limpei a senha de todo mundo que o banco achou
        List<UserResponseDTO> usuariosLimpos = usuariosDoBanco.stream()
                .map(user -> new UserResponseDTO(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole().name()
                ))
                .toList();

        // 3. Devolvemos a lista limpa
        return ResponseEntity.ok(usuariosLimpos);
    }
    // ==========================================
    // ROTA DE ATUALIZAR USUÁRIO (PUT)
    // ==========================================
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID id, @RequestBody UserRequestDTO requestDTO) {

        // 1. O garçom vai no banco verificar se esse cara realmente existe
        User usuarioDoBanco = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        // 2.  sujei a entidade com os dados novos que vieram do Postman
        usuarioDoBanco.setName(requestDTO.name());
        usuarioDoBanco.setEmail(requestDTO.email());
        usuarioDoBanco.setRole(requestDTO.role());
        //3 como o usuário já tem um ID, o ".save()" faz um UPDATE em vez de um INSERT!
        User usuarioAtualizado = userRepository.save(usuarioDoBanco);

        // 4. Empratar a resposta segura
        UserResponseDTO resposta = new UserResponseDTO(
                usuarioAtualizado.getId(),
                usuarioAtualizado.getName(),
                usuarioAtualizado.getEmail(),
                usuarioAtualizado.getRole().name()
        );

        // 5. Devolver para o cliente
        return ResponseEntity.ok(resposta);
    }

    // ==========================================
    // ROTA DE DELETAR USUÁRIO (DELETE)
    // ==========================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {

        // 1. Verificar se o usuário existe antes de tentar deletar
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado!");
        }

        // 2. A guilhotina do banco de dados (Deleta de verdade)
        userRepository.deleteById(id);

        // 3. Devolver o Status 204 (No Content)
        return ResponseEntity.noContent().build();
    }
}
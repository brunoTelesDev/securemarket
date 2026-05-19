package com.securemarket.controller;

import com.securemarket.dto.LoginRequestDTO;
import com.securemarket.dto.LoginResponseDTO;
import com.securemarket.entity.User;
import com.securemarket.repository.UserRepository;
import com.securemarket.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth") // Rota principal: http://localhost:8080/auth/login
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO requestDTO) {

        // 1. Procura o usuário pelo e-mail no banco (Se não achar, dá erro)
        User user = userRepository.findByEmail(requestDTO.email())
                .orElseThrow(() -> new RuntimeException("E-mail ou senha inválidos"));

        // 2. O triturador (BCrypt) compara a senha digitada com a criptografada do banco
        if (!passwordEncoder.matches(requestDTO.password(), user.getPassword())) {
            throw new RuntimeException("E-mail ou senha inválidos"); // Uso a mesma mensagem por segurança contra hackers
        }

        // 3. Se passou pela validação, a máquina gera a pulseira VIP (Token JWT)
        String token = tokenService.generateToken(user);

        // 4. Devolve o token dentro da bandeja de resposta
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}
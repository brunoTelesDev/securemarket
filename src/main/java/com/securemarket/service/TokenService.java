package com.securemarket.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.securemarket.entity.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    private final String secret = "minha_senha_secreta_super_forte";

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("SecureMarket API")
                    .withSubject(user.getEmail())
                    .withExpiresAt(gerarDataExpiracao())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar o token JWT", exception);
        }
    }

    private Instant gerarDataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));

    }
    // ==========================================
    // MÉTODO QUE LÊ E VALIDA A PULSEIRA VIP
    // ==========================================
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("SecureMarket API")
                    .build()
                    .verify(token)
                    .getSubject(); // Pega o e-mail (Subject) que guardamos lá dentro
        } catch (com.auth0.jwt.exceptions.JWTVerificationException exception) {
            return ""; // Se a pulseira for falsa, adulterada ou estiver vencida, barra a entrada!
        }
    }
}
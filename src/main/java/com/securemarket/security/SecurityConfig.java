package com.securemarket.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Avisa o Spring que isso é um arquivo de configurações
@EnableWebSecurity // Liga a segurança na nossa API
public class SecurityConfig {

    // Injeta o nosso Leitor de Pulseiras que criamos agorinha
    private final SecurityFilter securityFilter;

    public SecurityConfig(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    // ==========================================
    // REGRAS DE PORTA (QUEM ENTRA E QUEM SAI)
    // ==========================================
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Desliga proteção contra formulários falsos (usamos API, não precisa)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Diz que a API não guarda sessão, só aceita Token
                .authorizeHttpRequests(auth -> auth
                        // 🟢 Rotas Públicas (Não precisa de Token)
                        .requestMatchers(HttpMethod.POST, "/users").permitAll() // Criar usuário
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll() // Fazer login

                        // 🔴 Rotas Protegidas (Tudo o que sobrar, EXIGE o Token)
                        .anyRequest().authenticated()
                )
                // Coloca o nosso segurança do JWT na frente da porta ANTES do segurança do Spring
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // ==========================================
    // TRITURADOR DE SENHAS (BCrypt)
    // ==========================================
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
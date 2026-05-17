package com.securemarket.config; // Ajuste se o seu pacote for apenas com.securemarket.config

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// ==========================================
// AS REGRAS DO SEGURANÇA DA PORTA
// ==========================================
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Desliga a proteção contra formulários web antigos ( API moderna não precisa disso)
                .csrf(csrf -> csrf.disable())

                // Configura as portas
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.POST, "/users").permitAll()

                        .requestMatchers(HttpMethod.GET, "/users/**").permitAll()

                        .requestMatchers(HttpMethod.PUT, "/users/**").permitAll()

                        .requestMatchers(HttpMethod.DELETE, "/users/**").permitAll()

                        //tranca todo o resto
                        .anyRequest().authenticated()
                )
                .build();
    }
    // Ensinando o Spring a criar o triturador BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
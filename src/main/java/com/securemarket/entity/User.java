package com.securemarket.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// ✅ NOVO: O implements UserDetails é o Crachá do Spring Security
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private boolean active = true;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    // ==========================================
    // ✅ NOVO: MÉTODOS OBRIGATÓRIOS DO USERDETAILS
    // ==========================================

    // 1. Diz pro Segurança qual é o nível de acesso (Role) desse cara
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UserRole.ADMIN) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        } else if (this.role == UserRole.SELLER) {
            return List.of(new SimpleGrantedAuthority("ROLE_SELLER"), new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    // 2. O Spring pergunta: "Qual é o login dele?" (Nós usamos o e-mail)
    @Override
    public String getUsername() {
        return this.email;
    }

    // 3. Daqui pra baixo, dizemos que a conta está sempre válida e desbloqueada (por enquanto)
    @Override
    public boolean isAccountNonExpired() {
        return true; // Conta não expirou
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Conta não está bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Senha não expirou
    }

    @Override
    public boolean isEnabled() {
        return this.active; // Conta está ativa de verdade? (Pega da nossa variável active)
    }
}
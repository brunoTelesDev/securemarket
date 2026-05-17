package com.securemarket.entity;

// Importa ferramentas do JPA (para conversar com o banco)
import jakarta.persistence.*;
// Importa o Lombok (para não escrever Getters e Setters na mão)
        import lombok.*;
//  para trabalhar com datas e gerar o ID seguro
        import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;


    // @Enumerated(EnumType.STRING): Avisa o banco de dados para salvar a opção escolhida como texto (ex: "ADMIN").
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    // GATILHOS DE AUDITORIA AUTOMÁTICA

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
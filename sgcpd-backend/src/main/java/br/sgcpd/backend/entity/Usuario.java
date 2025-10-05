package br.sgcpd.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "usuario",
        uniqueConstraints = @UniqueConstraint(name = "uk_user_email", columnNames = "email"))
public class Usuario {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 160)
    private String nome;

    @Column(nullable = false, length = 160)
    private String email;

    @Column(nullable = false, length = 160)
    private String senha;

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(nullable = false, updatable = false)
    private Instant dataCriacao = Instant.now();

    @Column(name = "data_atualizacao", nullable = false)
    private Instant dataAtualizacao = Instant.now();

    @PreUpdate
    public void onUpdate() { this.dataAtualizacao = Instant.now(); }

}


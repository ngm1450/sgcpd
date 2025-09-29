package br.uniesquina.backend.model;

import br.uniesquina.backend.enums.StatusUsuarioEnum;
import br.uniesquina.backend.enums.TipoPessoaEnum;
import br.uniesquina.backend.model.Pessoa;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuario")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_pessoa", unique = true, nullable = false)
    private Pessoa pessoa;

    @Column(name = "hash_senha", nullable = false)
    private String hashSenha;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusUsuarioEnum status = StatusUsuarioEnum.ATIVO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPessoaEnum tipoPessoa;

    @Column(name = "ultimo_login")
    private LocalDateTime ultimoLogin;
}

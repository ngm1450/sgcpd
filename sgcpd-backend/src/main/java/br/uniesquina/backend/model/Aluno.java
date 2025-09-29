package br.uniesquina.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "aluno")
@PrimaryKeyJoinColumn(name = "id_pessoa")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Aluno extends Pessoa {

    @Column(unique = true, nullable = false)
    private String matricula;

    private String situacao;

    private Integer limiteCreditos;

    @Column(name = "foto_perfil", columnDefinition = "text")
    private String fotoPerfil;

    @ManyToOne
    @JoinColumn(name = "id_curso")
    private Curso curso;
}

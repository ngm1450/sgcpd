package br.uniesquina.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "professor")
@PrimaryKeyJoinColumn(name = "id_pessoa")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Professor extends Pessoa {

    @Column(unique = true, nullable = false)
    private String matricula;

    @ManyToOne
    @JoinColumn(name = "id_departamento", nullable = false)
    private Departamento departamento;

    @Column(name = "foto_perfil", columnDefinition = "text")
    private String fotoPerfil;
}


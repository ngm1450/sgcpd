package br.uniesquina.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "funcionario")
@PrimaryKeyJoinColumn(name = "id_pessoa")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Funcionario extends Pessoa {

    @Column(unique = true, nullable = false)
    private String matricula;

    private String cargo;

    @Column(name = "foto_perfil", columnDefinition = "text")
    private String fotoPerfil;

    @ManyToOne
    @JoinColumn(name = "id_departamento")
    private Departamento departamento;
}

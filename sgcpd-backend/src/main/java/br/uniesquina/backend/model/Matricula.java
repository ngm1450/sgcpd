package br.uniesquina.backend.model;

import br.uniesquina.backend.enums.StatusMatriculaEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_aluno", nullable = false)
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "id_turma", nullable = false)
    private Turma turma;

    @Enumerated(EnumType.STRING)
    private StatusMatriculaEnum statusMatricula;

    private LocalDateTime dataPedido;

    private LocalDateTime dataProcessamento;

    private LocalDateTime dataReprocessamento;
}


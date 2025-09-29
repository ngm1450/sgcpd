package br.uniesquina.backend.dto.professor;

import br.uniesquina.backend.dto.TurmaResumoDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfessorPaginaInicialDto {
    private String nome;
    private String matricula;
    private String departamento;
    private String fotoPerfil;
    private int totalTurmas;
    private int totalAlunos;
    private int lancamentosPendentes;
    private List<TurmaResumoDto> turmas;
}

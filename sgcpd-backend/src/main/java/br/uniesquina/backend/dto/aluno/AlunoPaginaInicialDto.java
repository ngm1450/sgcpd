package br.uniesquina.backend.dto.aluno;

import br.uniesquina.backend.dto.TurmaResumoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlunoPaginaInicialDto {
    private String nome;
    private String matricula;
    private String curso;
    private String departamento;
    private String semestreAtual;
    private int creditosConcluidos;
    private int creditosTotais;
    private double cr;
    private String previsaoFormatura;
    private String fotoPerfil;
    private List<TurmaResumoDto> turmas;

}

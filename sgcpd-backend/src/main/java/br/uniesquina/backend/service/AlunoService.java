package br.uniesquina.backend.service;

import br.uniesquina.backend.dto.aluno.AlunoPaginaInicialDto;
import br.uniesquina.backend.dto.TurmaResumoDto;
import br.uniesquina.backend.model.Aluno;
import br.uniesquina.backend.model.Turma;
import br.uniesquina.backend.repository.AlunoRepository;
import br.uniesquina.backend.repository.TurmaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final TurmaRepository turmaRepository;

    public AlunoPaginaInicialDto buscarDadosPaginaInicial(String matricula) {
        Aluno aluno = alunoRepository.findByMatricula(matricula)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado."));

        int anoAtual = 2025;
        int periodoAtual = 1;

        List<Turma> turmas = turmaRepository.findByAlunoAndSemestre(aluno.getId(), anoAtual, periodoAtual);

        List<TurmaResumoDto> turmasDto = turmas.stream().map(t ->
            new TurmaResumoDto(
                t.getDisciplina().getCodigo(),
                t.getDisciplina().getNome(),
                t.getHorario()
            )
        ).collect(Collectors.toList());

        return AlunoPaginaInicialDto.builder()
                .nome(aluno.getNome())
                .matricula(aluno.getMatricula())
                .curso(aluno.getCurso().getDescricao())
                .departamento(aluno.getCurso().getDepartamento().getNome())
                .semestreAtual("2025/1")
                .creditosConcluidos(50)
                .creditosTotais(200)
                .cr(8.2)
                .previsaoFormatura("2026/2")
                .fotoPerfil(aluno.getFotoPerfil())
                .turmas(turmasDto)
                .build();
    }
}


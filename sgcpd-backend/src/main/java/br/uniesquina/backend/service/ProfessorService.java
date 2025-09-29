package br.uniesquina.backend.service;

import br.uniesquina.backend.dto.TurmaResumoDto;
import br.uniesquina.backend.dto.professor.ProfessorPaginaInicialDto;
import br.uniesquina.backend.model.Professor;
import br.uniesquina.backend.model.Turma;
import br.uniesquina.backend.repository.ProfessorRepository;
import br.uniesquina.backend.repository.TurmaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final TurmaRepository turmaRepository;

    public ProfessorPaginaInicialDto buscarDadosPaginaInicial(String matricula) {
        Professor professor = professorRepository.findByMatricula(matricula)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado."));

        int anoAtual = 2025;
        int periodoAtual = 1;

        List<Turma> turmas = turmaRepository.findByProfessorAndSemestreAtual(professor.getId(), anoAtual, periodoAtual);

        List<TurmaResumoDto> turmasDto = turmas.stream()
                .map(t -> new TurmaResumoDto(
                        t.getDisciplina().getCodigo(),
                        t.getDisciplina().getNome(),
                        t.getHorario()
                ))
                .collect(Collectors.toList());

        return ProfessorPaginaInicialDto.builder()
                .nome(professor.getNome())
                .matricula(professor.getMatricula())
                .departamento(professor.getDepartamento().getNome())
                .fotoPerfil(professor.getFotoPerfil())
                .totalTurmas(turmas.size())
                .totalAlunos(12)
                .lancamentosPendentes(5)
                .turmas(turmasDto)
                .build();
    }
}

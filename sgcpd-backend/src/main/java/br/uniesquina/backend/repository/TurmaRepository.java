package br.uniesquina.backend.repository;

import br.uniesquina.backend.model.Turma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TurmaRepository extends JpaRepository<Turma, Long> {

    @Query("""
        SELECT t
        FROM Turma t
        JOIN t.matriculas m
        JOIN FETCH t.disciplina
        WHERE m.aluno.id = :alunoId
          AND t.semestre.ano = :ano
          AND t.semestre.periodo = :periodo
    """)
    List<Turma> findByAlunoAndSemestre(Long alunoId, int ano, int periodo);


    @Query("""
        SELECT t FROM Turma t
        WHERE t.professor.id = :idProfessor
        AND t.semestre.ano = :ano AND t.semestre.periodo = :periodo
    """)
    List<Turma> findByProfessorAndSemestreAtual(Long idProfessor, int ano, int periodo);


}

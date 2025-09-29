package br.uniesquina.backend.repository;

import br.uniesquina.backend.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    Optional<Professor> findByMatricula(String matricula);
}
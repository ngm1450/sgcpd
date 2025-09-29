package br.uniesquina.backend.repository;

import br.uniesquina.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("""
        SELECT u FROM Usuario u
        LEFT JOIN Aluno a ON u.pessoa = a
        LEFT JOIN Professor p ON u.pessoa = p
        LEFT JOIN Funcionario f ON u.pessoa = f
        WHERE a.matricula = :matricula OR p.matricula = :matricula OR f.matricula = :matricula
    """)
    Optional<Usuario> findByMatricula(String matricula);
}


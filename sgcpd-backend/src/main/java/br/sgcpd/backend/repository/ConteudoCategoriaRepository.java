package br.sgcpd.backend.repository;

import br.sgcpd.backend.entity.ConteudoCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConteudoCategoriaRepository extends JpaRepository<ConteudoCategoria, Long> {

    boolean existsByNomeIgnoreCase(String name);

    Optional<ConteudoCategoria> findByNomeIgnoreCase(String name);

}
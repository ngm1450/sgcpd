package br.sgcpd.backend.repository;

import br.sgcpd.backend.entity.Conteudo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConteudoRepository extends JpaRepository<Conteudo, Long>, ConteudoPesquisaRepository {

    @EntityGraph(attributePaths = {"autor", "categoria", "tags"})
    Page<Conteudo> findAll(Pageable pageable);

}

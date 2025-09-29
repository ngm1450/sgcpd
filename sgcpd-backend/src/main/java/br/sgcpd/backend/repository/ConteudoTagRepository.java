package br.sgcpd.backend.repository;

import br.sgcpd.backend.entity.ConteudoTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConteudoTagRepository extends JpaRepository<ConteudoTag, Long> {

    boolean existsByNomeIgnoreCase(String name);

    Optional<ConteudoTag> findByNomeIgnoreCase(String name);

    List<ConteudoTag> findByIdIn(Collection<Long> id);

}
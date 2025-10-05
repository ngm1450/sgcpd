package br.sgcpd.backend.repository;

import br.sgcpd.backend.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findTagsByIdCategoria(Long idCategoria);

    List<Tag> findByIdIn(Collection<Long> id);

}
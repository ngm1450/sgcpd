package br.sgcpd.backend.repository;

import br.sgcpd.backend.entity.Conteudo;
import br.sgcpd.backend.enums.StatusConteudoEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Set;

@Repository
public interface ConteudoRepository extends JpaRepository<Conteudo, Long> {

    @EntityGraph(attributePaths = {"autor", "categoria", "tags"})
    Page<Conteudo> findAll(Pageable pageable);

    @Query(
            value = """
                select distinct c.*
                from public.conteudo c
                left join public.conteudo_x_tag t on c.id = t.id_conteudo
                where (
                    cast(:textoBusca as text) is null
                    or lower(c.titulo) like lower(concat('%', :textoBusca, '%'))
                    or lower(cast(c.corpo as text)) like lower(concat('%', :textoBusca, '%'))
                )
                  and ( cast(:idCategoria as bigint) is null or c.id_categoria = :idCategoria )
                  and ( cast(:status as text)      is null or c.status       = cast(:status as text) )
                  and ( cast(:createdFrom as timestamptz) is null or c.data_criacao     >= :createdFrom )
                  and ( cast(:createdTo   as timestamptz) is null or c.data_atualizacao <  :createdTo   )
                  and ( :hasTags = false or t.id_tag in (:tagIds) )
                order by c.data_atualizacao asc, c.data_atualizacao asc
              """,
            countQuery = """
                select count(distinct c.id)
                from public.conteudo c
                left join public.conteudo_x_tag t on c.id = t.id_conteudo
                where (
                    cast(:textoBusca as text) is null
                    or lower(c.titulo) like lower(concat('%', :textoBusca, '%'))
                    or lower(cast(c.corpo as text)) like lower(concat('%', :textoBusca, '%'))
                )
                  and ( cast(:idCategoria as bigint) is null or c.id_categoria = :idCategoria )
                  and ( cast(:status as text)      is null or c.status       = cast(:status as text) )
                  and ( cast(:createdFrom as timestamptz) is null or c.data_criacao     >= :createdFrom )
                  and ( cast(:createdTo   as timestamptz) is null or c.data_atualizacao <  :createdTo   )
                  and ( :hasTags = false or t.id_tag in (:tagIds) )
              """,
            nativeQuery = true
    )
    Page<Conteudo> search(
        @Param("textoBusca")  String textoBusca,
        @Param("idCategoria") Long idCategoria,
        @Param("tagIds")      Set<Long> tagIds,      // passar Set vazio quando não filtrar
        @Param("hasTags")     boolean hasTags,       // true se tagIds não vazio
        @Param("status")      String status,         // se na tabela é TEXT, envie enum como String
        @Param("createdFrom") Instant createdFrom,
        @Param("createdTo")   Instant createdTo,
        Pageable pageable
    );




}

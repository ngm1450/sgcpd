package br.sgcpd.backend.repository;

import br.sgcpd.backend.entity.Conteudo;
import br.sgcpd.backend.enums.StatusConteudoEnum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Set;

public class ConteudoPesquisaRepositoryImpl implements ConteudoPesquisaRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Conteudo> search(String textoBusca,
                                 Long idCategoria,
                                 Set<Long> tagIds,
                                 StatusConteudoEnum status,
                                 Instant createdFrom,
                                 Instant createdTo,
                                 Pageable pageable) {

        String base = """
            select c from Conteudo c
              left join c.tags t
            where (:textoBusca is null or lower(c.titulo) like lower(concat('%', :textoBusca, '%'))
                               or lower(c.corpo)  like lower(concat('%', :textoBusca, '%')))
              and (:idCategoria is null or c.categoria.id = :idCategoria)
              and (:status is null or c.status = :status)
              and (:createdFrom is null or c.dataCriacao >= :createdFrom)
              and (:createdTo   is null or c.dataAtualizacao <  :createdTo)
        """;

        // if tagIds provided, require all tags OR at least one? Here: at least one
        String tagClause = (tagIds != null && !tagIds.isEmpty())
                ? " and t.id in :tagIds " : "";

        var query = em.createQuery(base + tagClause + " group by c", Conteudo.class);
        var count = em.createQuery("select count(distinct c) " + base + tagClause, Long.class);

        query.setParameter("textoBusca", textoBusca);
        count.setParameter("textoBusca", textoBusca);
        query.setParameter("idCategoria", idCategoria);
        count.setParameter("idCategoria", idCategoria);
        query.setParameter("status", status);
        count.setParameter("status", status);
        query.setParameter("createdFrom", createdFrom);
        count.setParameter("createdFrom", createdFrom);
        query.setParameter("createdTo", createdTo);
        count.setParameter("createdTo", createdTo);

        if (!tagClause.isEmpty()) {
            query.setParameter("tagIds", tagIds);
            count.setParameter("tagIds", tagIds);
        }

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        var list = query.getResultList();
        long total = count.getSingleResult();
        return new org.springframework.data.domain.PageImpl<>(list, pageable, total);
    }
}

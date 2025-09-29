package br.sgcpd.backend.repository;

import br.sgcpd.backend.entity.Conteudo;
import br.sgcpd.backend.enums.StatusConteudoEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Set;

public interface ConteudoPesquisaRepository {

    Page<Conteudo> search(
        String text,
        Long categoryId,
        Set<Long> tagIds,
        StatusConteudoEnum status,
        Instant createdFrom,
        Instant createdTo,
        Pageable pageable
    );

}

package br.sgcpd.backend.dto.conteudo;

import br.sgcpd.backend.enums.StatusConteudoEnum;

import java.time.Instant;
import java.util.Set;

public record ConteudoBuscaRequest(
    String textoBusca,
    Long idCategoria,
    Set<Long> idsTags,
    StatusConteudoEnum status,
    Instant createdFrom,
    Instant createdTo,
    Integer page,
    Integer size,
    String sort
) {}

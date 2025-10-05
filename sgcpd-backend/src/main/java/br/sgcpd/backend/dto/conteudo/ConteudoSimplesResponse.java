package br.sgcpd.backend.dto.conteudo;

import br.sgcpd.backend.enums.StatusConteudoEnum;

import java.time.Instant;

public record ConteudoSimplesResponse(
    Long id,
    String titulo,
    StatusConteudoEnum status,
    String nomeAutor,
    String nomeCategoria,
    Instant dataAtualizacao
) {}
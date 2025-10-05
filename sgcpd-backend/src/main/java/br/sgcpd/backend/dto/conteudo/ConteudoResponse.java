package br.sgcpd.backend.dto.conteudo;

import br.sgcpd.backend.entity.Arquivo;
import br.sgcpd.backend.enums.StatusConteudoEnum;

import java.time.Instant;
import java.util.List;
import java.util.Set;

public record ConteudoResponse(
    Long id,
    String titulo,
    String corpo,
    StatusConteudoEnum status,
    Long idAutor,
    String nomeAutor,
    Long idCategoria,
    String nomeCategoria,
    Set<Long> idsTags,
    Set<String> tagNames,
    Instant dataCriacao,
    Instant dataAtualizacao,
    List<Arquivo> arquivos
) {}
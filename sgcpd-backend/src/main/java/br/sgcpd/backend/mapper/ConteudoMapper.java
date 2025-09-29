package br.sgcpd.backend.mapper;

import br.sgcpd.backend.dto.conteudo.ConteudoAlteracaoRequest;
import br.sgcpd.backend.dto.conteudo.ConteudoResponse;
import br.sgcpd.backend.entity.Conteudo;
import br.sgcpd.backend.entity.ConteudoCategoria;
import br.sgcpd.backend.entity.ConteudoTag;

import java.util.Set;
import java.util.stream.Collectors;

public class ConteudoMapper {

    private  ConteudoMapper() {}

    public static ConteudoResponse toResponse(Conteudo c) {
        Set<Long> tagIds = c.getTags().stream().map(ConteudoTag::getId).collect(Collectors.toSet());
        Set<String> tagNames = c.getTags().stream().map(ConteudoTag::getNome).collect(Collectors.toSet());
        return new ConteudoResponse(
            c.getId(), c.getTitulo(), c.getCorpo(), c.getStatus(),
            c.getAutor().getId(), c.getAutor().getNome(),
            c.getCategoria() != null ? c.getCategoria().getId() : null,
            c.getCategoria() != null ? c.getCategoria().getNome() : null,
            tagIds, tagNames, c.getDataCriacao(), c.getDataAtualizacao()
        );
    }

    public static void apply(Conteudo c, ConteudoAlteracaoRequest r, ConteudoCategoria category, Set<ConteudoTag> tags) {
        c.setTitulo(r.titulo());
        c.setCorpo(r.corpo());
        c.setStatus(r.status());
        c.setCategoria(category);
        c.getTags().clear();
        if (tags != null) c.getTags().addAll(tags);
    }

}

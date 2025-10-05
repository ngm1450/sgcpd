package br.sgcpd.backend.mapper;

import br.sgcpd.backend.dto.conteudo.ConteudoAlteracaoRequest;
import br.sgcpd.backend.dto.conteudo.ConteudoResponse;
import br.sgcpd.backend.dto.conteudo.ConteudoSimplesResponse;
import br.sgcpd.backend.entity.Conteudo;
import br.sgcpd.backend.entity.Categoria;
import br.sgcpd.backend.entity.Tag;
import br.sgcpd.backend.entity.Usuario;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ConteudoMapper {

    private  ConteudoMapper() {}

    public static ConteudoResponse toCompleteResponse(Conteudo c) {
        Set<Long> tagIds = c.getTags().stream().map(Tag::getId).collect(Collectors.toSet());
        Set<String> tagNames = c.getTags().stream().map(Tag::getNome).collect(Collectors.toSet());
        return new ConteudoResponse(
            c.getId(),
            c.getTitulo(),
            c.getCorpo(),
            c.getStatus(),
            c.getAutor().getId(),
            c.getAutor().getNome(),
            c.getCategoria() != null ? c.getCategoria().getId() : null,
            c.getCategoria() != null ? c.getCategoria().getNome() : null,
            tagIds,
            tagNames,
            c.getDataCriacao(),
            c.getDataAtualizacao(),
            c.getArquivos().stream().toList()
        );
    }

    public static ConteudoSimplesResponse toSimpleResponse(Conteudo c) {
        return new ConteudoSimplesResponse(
            c.getId(),
            c.getTitulo(),
            c.getStatus(),
            c.getAutor().getNome(),
            c.getCategoria().getNome(),
            c.getDataAtualizacao()
        );
    }

    public static void apply(Conteudo c, ConteudoAlteracaoRequest r, Categoria category, Set<Tag> tags, Usuario author) {
        c.setTitulo(r.titulo());
        c.setAutor(author);
        c.setCorpo(r.corpo());
        c.setStatus(r.status());
        c.setCategoria(category);
        c.getTags().clear();
        if (tags != null) c.getTags().addAll(tags);
    }

}

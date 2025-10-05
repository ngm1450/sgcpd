package br.sgcpd.backend.mapper;

import br.sgcpd.backend.dto.categoria.ConteudoCategoriaRequest;
import br.sgcpd.backend.dto.categoria.ConteudoCategoriaResponse;
import br.sgcpd.backend.entity.Categoria;

public class ConteudoCategoriaMapper {

    private ConteudoCategoriaMapper() {}

    public static ConteudoCategoriaResponse toResponse(Categoria c) {
        return new ConteudoCategoriaResponse(c.getId(), c.getNome(), c.getDescricao());
    }

    public static void apply(Categoria c, ConteudoCategoriaRequest r) {
        c.setNome(r.nome());
        c.setDescricao(r.descricao());
    }
}

package br.sgcpd.backend.mapper;

import br.sgcpd.backend.dto.usuario.UsuarioAlteracaoRequest;
import br.sgcpd.backend.dto.usuario.UsuarioResposta;
import br.sgcpd.backend.entity.Usuario;

public class UsuarioMapper {

    private UsuarioMapper() {}

    public static UsuarioResposta toResponse(Usuario u) {
        return new UsuarioResposta(u.getId(), u.getNome(), u.getEmail(), u.isAtivo());
    }

    public static void update(Usuario u, UsuarioAlteracaoRequest r) {
        u.setNome(r.nome());
        u.setEmail(r.email());
        if (r.ativo() != null) u.setAtivo(r.ativo());
    }

}

package br.sgcpd.backend.dto.usuario;

public record UsuarioResposta(
    Long id, String nome, String email, boolean ativo
) {}

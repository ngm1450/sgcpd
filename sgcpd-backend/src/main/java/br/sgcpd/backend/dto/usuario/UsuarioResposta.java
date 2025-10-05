package br.sgcpd.backend.dto.usuario;

import java.time.Instant;

public record UsuarioResposta(
    Long id,
    String nome,
    String email,
    boolean ativo,
    Instant dataCriacao
) {}

package br.sgcpd.backend.dto.usuario;

import jakarta.validation.constraints.*;

public record UsuarioAlteracaoRequest(
    @NotBlank @Size(max = 160) String nome,
    @NotBlank @Email @Size(max = 160) String email,
    Boolean ativo
) {}

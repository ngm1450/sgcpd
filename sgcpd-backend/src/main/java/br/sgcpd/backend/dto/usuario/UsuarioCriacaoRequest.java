package br.sgcpd.backend.dto.usuario;

import jakarta.validation.constraints.*;

public record UsuarioCriacaoRequest(
    @NotBlank @Size(max = 160) String name,
    @NotBlank @Email @Size(max = 160) String email
) {}
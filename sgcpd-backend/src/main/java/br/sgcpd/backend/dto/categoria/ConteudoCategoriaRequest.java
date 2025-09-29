package br.sgcpd.backend.dto.categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ConteudoCategoriaRequest(
    @NotBlank @Size(max = 80) String nome,
    @Size(max = 240) String descricao
) {}

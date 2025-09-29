package br.sgcpd.backend.dto.conteudo;

import br.sgcpd.backend.enums.StatusConteudoEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record ConteudoAlteracaoRequest(
    @NotBlank @Size(max = 160) String titulo,
    @NotBlank String corpo,
    Long idCategoria,
    Set<Long> idsTags,
    @NotNull StatusConteudoEnum status
) {}

package br.sgcpd.backend.dto.tag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ConteudoTagRequest(@NotBlank @Size(max = 60) String name) {}

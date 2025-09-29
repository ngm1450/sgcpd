package br.uniesquina.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TurmaResumoDto {
    private String codigo;
    private String nome;
    private String horario;
}

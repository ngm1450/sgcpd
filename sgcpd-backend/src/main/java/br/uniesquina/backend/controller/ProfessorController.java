package br.uniesquina.backend.controller;

import br.uniesquina.backend.dto.professor.ProfessorPaginaInicialDto;
import br.uniesquina.backend.service.ProfessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/professores")
@RequiredArgsConstructor
public class ProfessorController {

    private final ProfessorService professorService;

    @GetMapping("/pagina-inicial/{matricula}")
    public ProfessorPaginaInicialDto buscarPaginaInicial(@PathVariable String matricula) {
        return professorService.buscarDadosPaginaInicial(matricula);
    }
}

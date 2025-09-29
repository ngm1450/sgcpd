package br.uniesquina.backend.controller;

import br.uniesquina.backend.dto.aluno.AlunoPaginaInicialDto;
import br.uniesquina.backend.service.AlunoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alunos")
@RequiredArgsConstructor
public class AlunoController {

    private final AlunoService alunoService;

    @GetMapping("/pagina-inicial")
    public ResponseEntity<AlunoPaginaInicialDto> getPaginaInicial(@RequestParam String matricula) {
        AlunoPaginaInicialDto dto = alunoService.buscarDadosPaginaInicial(matricula);
        return ResponseEntity.ok(dto);
    }
}

package br.sgcpd.backend.controller;

import br.sgcpd.backend.service.ArquivoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/arquivos")
public class ArquivoController {

    private final ArquivoService arquivoService;

    @GetMapping("/buscar-dados/{id}")
    public ResponseEntity<byte[]> buscarDadosPorId(@PathVariable Long id) {
        byte[] dados = arquivoService.buscarDadosPorId(id);
        String nome = arquivoService.buscarNomePorId(id);

        return ResponseEntity.ok()
                .header("Content-Type", "application/octet-stream")
                .header("Content-Disposition", "attachment; filename=" + nome)
                .body(dados);
    }

}

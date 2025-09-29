package br.sgcpd.backend.controller;

import br.sgcpd.backend.dto.conteudo.ConteudoAlteracaoRequest;
import br.sgcpd.backend.dto.conteudo.ConteudoBuscaRequest;
import br.sgcpd.backend.dto.conteudo.ConteudoCriacaoRequest;
import br.sgcpd.backend.dto.conteudo.ConteudoResponse;
import br.sgcpd.backend.enums.StatusConteudoEnum;
import br.sgcpd.backend.service.ConteudoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/conteudos")
public class ConteudoController {

    private final ConteudoService service;

    @PostMapping
    public ResponseEntity<ConteudoResponse> create(@Valid @RequestBody ConteudoCriacaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    public ConteudoResponse update(@PathVariable Long id, @Valid @RequestBody ConteudoAlteracaoRequest request) {
        return service.update(id, request);
    }

    @GetMapping("/{id}")
    public ConteudoResponse findById(@PathVariable Long id) { return service.findById(id); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }

    @GetMapping
    public Page<ConteudoResponse> search(
        @RequestParam(required = false) String textoBusca,
        @RequestParam(required = false) Long idCategoria,
        @RequestParam(required = false) Set<Long> idsTags,
        @RequestParam(required = false) StatusConteudoEnum status,
        @RequestParam(required = false) Instant createdFrom,
        @RequestParam(required = false) Instant createdTo,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sort
    ) {
        var params = new ConteudoBuscaRequest(textoBusca, idCategoria, idsTags, status, createdFrom, createdTo, page, size, sort);
        return service.search(params);
    }

}

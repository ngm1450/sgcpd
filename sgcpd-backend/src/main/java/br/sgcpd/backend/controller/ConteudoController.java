package br.sgcpd.backend.controller;

import br.sgcpd.backend.dto.PageResponse;
import br.sgcpd.backend.dto.conteudo.ConteudoAlteracaoRequest;
import br.sgcpd.backend.dto.conteudo.ConteudoBuscaRequest;
import br.sgcpd.backend.dto.conteudo.ConteudoCriacaoRequest;
import br.sgcpd.backend.dto.conteudo.ConteudoResponse;
import br.sgcpd.backend.dto.conteudo.ConteudoSimplesResponse;
import br.sgcpd.backend.enums.StatusConteudoEnum;
import br.sgcpd.backend.service.ConteudoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/conteudos")
public class ConteudoController {

    private final ConteudoService service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ConteudoSimplesResponse> create(
        @Valid @RequestPart("conteudo") ConteudoCriacaoRequest request,
        @RequestPart(name = "files", required = false) List<MultipartFile> files
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(request, files));
    }

    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ConteudoSimplesResponse update(
        @PathVariable Long id,
        @Valid @RequestPart("conteudo") ConteudoAlteracaoRequest request,
        @RequestPart(name = "files", required = false) List<MultipartFile> files
    ) {
        return service.update(id, request, files);
    }

    @GetMapping("/{id}")
    public ConteudoResponse findById(@PathVariable Long id) { return service.findById(id); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }

    @GetMapping
    public PageResponse<ConteudoSimplesResponse> search(
        @RequestParam(required = false) String textoBusca,
        @RequestParam(required = false) Long idCategoria,
        @RequestParam(required = false) Set<Long> idsTags,
        @RequestParam(required = false) StatusConteudoEnum status,
        @RequestParam(required = false) Instant createdFrom,
        @RequestParam(required = false) Instant createdTo,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "12") Integer size,
        @RequestParam(required = false) String sort
    ) {
        var params = new ConteudoBuscaRequest(textoBusca, idCategoria, idsTags, status, createdFrom, createdTo, page, size, sort);
        var result = service.search(params);
        return PageResponse.from(result);
    }


}

package br.sgcpd.backend.controller;

import br.sgcpd.backend.dto.PageResponse;
import br.sgcpd.backend.dto.usuario.UsuarioAlteracaoRequest;
import br.sgcpd.backend.dto.usuario.UsuarioCriacaoRequest;
import br.sgcpd.backend.dto.usuario.UsuarioResposta;
import br.sgcpd.backend.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final PasswordEncoder passwordEncoder;

    private final UsuarioService usuarioService;

    @GetMapping("/hash-senha")
    public String gerarHashSenha(@RequestParam String senha) {
        return passwordEncoder.encode(senha);
    }

    @PostMapping
    public ResponseEntity<UsuarioResposta> create(@Valid @RequestBody UsuarioCriacaoRequest request) {
        var created = usuarioService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public UsuarioResposta update(@PathVariable Long id, @Valid @RequestBody UsuarioAlteracaoRequest request) {
        return usuarioService.update(id, request);
    }

    @GetMapping("/{id}")
    public UsuarioResposta get(@PathVariable Long id) { return usuarioService.get(id); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { usuarioService.delete(id); }

    @GetMapping
    public PageResponse<UsuarioResposta> list(
        @RequestParam(required = false) String q,
        @RequestParam(required = false) Boolean ativo,
        @PageableDefault(size = 20) Pageable pageable
    ) {
        var page = usuarioService.list(q, ativo, pageable);
        return PageResponse.from(page);
    }

}


package br.sgcpd.backend.service;

import br.sgcpd.backend.dto.usuario.UsuarioAlteracaoRequest;
import br.sgcpd.backend.dto.usuario.UsuarioCriacaoRequest;
import br.sgcpd.backend.dto.usuario.UsuarioResposta;
import br.sgcpd.backend.entity.Usuario;
import br.sgcpd.backend.mapper.UsuarioMapper;
import br.sgcpd.backend.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static br.sgcpd.backend.mapper.UsuarioMapper.toResponse;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioResposta create(UsuarioCriacaoRequest request) {
        if (usuarioRepository.existsByEmailIgnoreCase(request.email()))
            throw new IllegalArgumentException("Email already in use");

        var u = new Usuario();
        u.setNome(request.name());
        u.setEmail(request.email());
        u = usuarioRepository.save(u);

        return toResponse(u);
    }

    public UsuarioResposta update(Long id, UsuarioAlteracaoRequest request) {
        var u = usuarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!u.getEmail().equalsIgnoreCase(request.email()) && usuarioRepository.existsByEmailIgnoreCase(request.email()))
            throw new IllegalArgumentException("Email already in use");

        UsuarioMapper.update(u, request);
        return toResponse(u);
    }

    public void delete(Long id) { usuarioRepository.deleteById(id); }

    public UsuarioResposta get(Long id) {
        return usuarioRepository.findById(id).map(UsuarioMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public Page<UsuarioResposta> list(Pageable pageable) {
        return usuarioRepository.findAll(pageable).map(UsuarioMapper::toResponse);
    }

}

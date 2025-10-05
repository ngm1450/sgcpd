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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static br.sgcpd.backend.mapper.UsuarioMapper.toResponse;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    public UsuarioResposta create(UsuarioCriacaoRequest request) {
        if (usuarioRepository.existsByEmailIgnoreCase(request.email()))
            throw new IllegalArgumentException("Email já em uso.");

        var u = new Usuario();

        u.setNome(request.nome());
        u.setSenha(passwordEncoder.encode(request.senha()));
        u.setEmail(request.email());

        u = usuarioRepository.save(u);

        return toResponse(u);
    }

    public UsuarioResposta update(Long id, UsuarioAlteracaoRequest request) {
        var u = usuarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));

        if (!u.getEmail().equalsIgnoreCase(request.email()) && usuarioRepository.existsByEmailIgnoreCase(request.email()))
            throw new IllegalArgumentException("Email já em uso.");

        if (request.senha() != null && !request.senha().trim().isEmpty()) {
            u.setSenha(passwordEncoder.encode(request.senha()));
        }

        UsuarioMapper.update(u, request);

        u = usuarioRepository.save(u);

        return toResponse(u);
    }

    public void delete(Long id) { usuarioRepository.deleteById(id); }

    public UsuarioResposta get(Long id) {
        return usuarioRepository.findById(id).map(UsuarioMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public Page<UsuarioResposta> list(String q, Boolean ativo, Pageable pageable) {
        var spec = where(UsuarioSpecs.qLike(q)).and(UsuarioSpecs.ativoEq(ativo));
        return usuarioRepository.findAll(spec, pageable)
                .map(UsuarioMapper::toResponse);
    }

    public static final class UsuarioSpecs {

        private UsuarioSpecs() {}

        public static Specification<Usuario> qLike(String q) {
            return (root, cq, cb) -> {
                if (q == null || q.isBlank()) return cb.conjunction();
                String like = "%" + q.trim().toLowerCase() + "%";
                return cb.or(
                    cb.like(cb.lower(root.get("nome")), like),
                    cb.like(cb.lower(root.get("email")), like)
                );
            };
        }

        public static Specification<Usuario> ativoEq(Boolean ativo) {
            return (root, cq, cb) -> {
                if (ativo == null) return cb.conjunction();
                return cb.equal(root.get("ativo"), ativo);
            };
        }
    }
}

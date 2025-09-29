package br.sgcpd.backend.service;

import br.sgcpd.backend.dto.conteudo.ConteudoAlteracaoRequest;
import br.sgcpd.backend.dto.conteudo.ConteudoBuscaRequest;
import br.sgcpd.backend.dto.conteudo.ConteudoCriacaoRequest;
import br.sgcpd.backend.dto.conteudo.ConteudoResponse;
import br.sgcpd.backend.entity.Conteudo;
import br.sgcpd.backend.entity.ConteudoCategoria;
import br.sgcpd.backend.entity.ConteudoTag;
import br.sgcpd.backend.mapper.ConteudoMapper;
import br.sgcpd.backend.repository.ConteudoCategoriaRepository;
import br.sgcpd.backend.repository.ConteudoRepository;
import br.sgcpd.backend.repository.ConteudoTagRepository;
import br.sgcpd.backend.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

import static br.sgcpd.backend.mapper.ConteudoMapper.apply;
import static br.sgcpd.backend.mapper.ConteudoMapper.toResponse;

@Service
@RequiredArgsConstructor
public class ConteudoService {

    private final ConteudoRepository conteudoRepository;

    private final UsuarioRepository usuarioRepository;

    private final ConteudoCategoriaRepository conteudoCategoriaRepository;

    private final ConteudoTagRepository conteudoTagRepository;

    public ConteudoResponse create(ConteudoCriacaoRequest r) {

        var author = usuarioRepository.findById(r.idAutor())
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));

        ConteudoCategoria categoria = null;
        if (r.idCategoria() != null) {
            categoria = conteudoCategoriaRepository.findById(r.idCategoria())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        }

        var tags = (r.idsTags() == null || r.idsTags().isEmpty())
                ? new HashSet<ConteudoTag>() : new HashSet<>(conteudoTagRepository.findByIdIn(r.idsTags()));

        var c = new Conteudo();
        c.setAutor(author);
        c.setTitulo(r.titulo());
        c.setCorpo(r.corpo());
        c.setStatus(r.status());
        c.setCategoria(categoria);
        c.getTags().addAll(tags);

        return toResponse(conteudoRepository.save(c));
    }

    public ConteudoResponse update(Long id, ConteudoAlteracaoRequest r) {
        var c = conteudoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Content not found"));

        ConteudoCategoria category = null;
        if (r.idCategoria() != null) {
            category = conteudoCategoriaRepository.findById(r.idCategoria())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        }

        var tags = (r.idsTags() == null || r.idsTags().isEmpty())
                ? new HashSet<ConteudoTag>() : new HashSet<>(conteudoTagRepository.findByIdIn(r.idsTags()));

        apply(c, r, category, tags);

        return toResponse(c);
    }

    public void delete(Long id) { conteudoRepository.deleteById(id); }

    public ConteudoResponse findById(Long id) {
        return toResponse(conteudoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Content not found")));
    }

    public Page<ConteudoResponse> search(ConteudoBuscaRequest p) {
        var page = p.page() == null ? 0 : p.page();
        var size = p.size() == null ? 20 : p.size();
        Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size,
                p.sort() != null ? Sort.by(p.sort().split(",")) : Sort.by(Sort.Direction.DESC, "createdAt"));

        var result = conteudoRepository.search(
            emptyToNull(p.textoBusca()),
            p.idCategoria(),
            p.idsTags(),
            p.status(),
            p.createdFrom(),
            p.createdTo(),
            pageable
        );

        return result.map(ConteudoMapper::toResponse);
    }

    private static String emptyToNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }

}

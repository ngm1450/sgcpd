package br.sgcpd.backend.service;

import br.sgcpd.backend.dto.conteudo.ConteudoAlteracaoRequest;
import br.sgcpd.backend.dto.conteudo.ConteudoBuscaRequest;
import br.sgcpd.backend.dto.conteudo.ConteudoCriacaoRequest;
import br.sgcpd.backend.dto.conteudo.ConteudoResponse;
import br.sgcpd.backend.dto.conteudo.ConteudoSimplesResponse;
import br.sgcpd.backend.entity.Conteudo;
import br.sgcpd.backend.entity.Categoria;
import br.sgcpd.backend.entity.Tag;
import br.sgcpd.backend.mapper.ConteudoMapper;
import br.sgcpd.backend.repository.ArquivoRepository;
import br.sgcpd.backend.repository.CategoriaRepository;
import br.sgcpd.backend.repository.ConteudoRepository;
import br.sgcpd.backend.repository.TagRepository;
import br.sgcpd.backend.utils.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static br.sgcpd.backend.mapper.ConteudoMapper.apply;
import static br.sgcpd.backend.mapper.ConteudoMapper.toCompleteResponse;
import static br.sgcpd.backend.mapper.ConteudoMapper.toSimpleResponse;

@Service
@RequiredArgsConstructor
public class ConteudoService {

    private final ConteudoRepository conteudoRepository;

    private final ArquivoRepository arquivoRepository;

    private final CategoriaRepository categoriaRepository;

    private final TagRepository conteudoTagRepository;

    private final SecurityUtils securityUtils;

    @Transactional
    public ConteudoSimplesResponse create(ConteudoCriacaoRequest r, List<MultipartFile> files) {

        var author = securityUtils.getAuthenticatedUser();

        Categoria categoria = categoriaRepository.findById(r.idCategoria())
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada."));

        var tags = new HashSet<>(conteudoTagRepository.findByIdIn(r.idsTags()));

        var c = new Conteudo();
        c.setAutor(author);
        c.setTitulo(r.titulo());
        c.setCorpo(r.corpo());
        c.setStatus(r.status());
        c.setCategoria(categoria);
        c.getTags().addAll(tags);

        // 1) salva o conteúdo
        c = conteudoRepository.save(c);

        // 2) trata anexos (se enviados)

        salvarAnexosSeHouver(c, files);

        return toSimpleResponse(c);

    }

    @Transactional
    public ConteudoSimplesResponse update(Long id, ConteudoAlteracaoRequest r, List<MultipartFile> files) {
        var c = conteudoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Content not found"));

        Categoria category = null;
        if (r.idCategoria() != null) {
            category = categoriaRepository.findById(r.idCategoria())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        }

        var tags = (r.idsTags() == null || r.idsTags().isEmpty())
                ? new HashSet<Tag>() : new HashSet<>(conteudoTagRepository.findByIdIn(r.idsTags()));

        var author = securityUtils.getAuthenticatedUser();

        // aplica mudanças básicas
        apply(c, r, category, tags, author);

        // salva conteúdo
        c = conteudoRepository.save(c);

        // adiciona novos anexos (se vierem)
        salvarAnexosSeHouver(c, files);

        return toSimpleResponse(conteudoRepository.save(c));
    }

    public void delete(Long id) { conteudoRepository.deleteById(id); }

    public ConteudoResponse findById(Long id) {
        return toCompleteResponse(conteudoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Content not found")));
    }

    public Page<ConteudoSimplesResponse> search(ConteudoBuscaRequest p) {
        int page = p.page() == null ? 0  : p.page();
        int size = p.size() == null ? 20 : p.size();

        Sort requested = (p.sort() != null && !p.sort().isBlank())
                ? Sort.by(p.sort().split(","))
                : Sort.by(Sort.Direction.DESC, "dataAtualizacao");

        Sort sqlSort = mapSortToSql(requested);

        Pageable pageable = PageRequest.of(page, size, sqlSort);

        // tags
        Set<Long> tagIds = p.idsTags();
        boolean hasTags  = tagIds != null && !tagIds.isEmpty();

        // status: a query nativa espera String (coluna é text/varchar)
        String statusStr = (p.status() == null ? null : p.status().name());

        var result = conteudoRepository.search(
            emptyToNull(p.textoBusca()),
            p.idCategoria(),
            (tagIds == null ? Set.of() : tagIds),
            hasTags,
            statusStr,
            p.createdFrom(),
            p.createdTo(),
            pageable
        );

        return result.map(ConteudoMapper::toSimpleResponse);
    }

    /** Converte Sort com campos do modelo para nomes de colunas no Postgres. */
    private Sort mapSortToSql(Sort sort) {
        if (sort == null || sort.isUnsorted()) {
            return Sort.unsorted();
        }

        Map<String, String> map = Map.of(
            "id",              "id",
            "titulo",          "titulo",
            "status",          "status",
            "dataCriacao",     "data_criacao",
            "dataAtualizacao", "data_atualizacao"
        );

        List<Sort.Order> orders = new ArrayList<>();
        for (Sort.Order o : sort) {
            String col = map.get(o.getProperty());
            if (col != null) {
                orders.add(new Sort.Order(o.getDirection(), col));
            }
        }
        return orders.isEmpty() ? Sort.unsorted() : Sort.by(orders);
    }

    /** Converte string vazia/blank para null (para casar com "cast(:param as ... ) is null"). */
    private String emptyToNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }

    @SneakyThrows
    private void salvarAnexosSeHouver(Conteudo conteudo, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) return;
        for (MultipartFile file : files) {
            arquivoRepository.inserirArquivo(
                file.getOriginalFilename(),
                file.getSize(),
                file.getBytes(),
                conteudo.getId()
            );
        }
    }

}

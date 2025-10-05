package br.sgcpd.backend.service;

import br.sgcpd.backend.entity.Tag;
import br.sgcpd.backend.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public List<Tag> findAll(Long idCategoria) {
        return idCategoria != null
                ? tagRepository.findTagsByIdCategoria(idCategoria)
                : tagRepository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
    }

}

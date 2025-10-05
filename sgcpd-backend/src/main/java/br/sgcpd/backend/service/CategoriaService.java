package br.sgcpd.backend.service;

import br.sgcpd.backend.entity.Categoria;
import br.sgcpd.backend.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public List<Categoria> findAll(){
        return categoriaRepository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
    }

}

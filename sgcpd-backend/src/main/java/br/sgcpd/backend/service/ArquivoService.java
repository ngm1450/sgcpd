package br.sgcpd.backend.service;

import br.sgcpd.backend.entity.Arquivo;
import br.sgcpd.backend.repository.ArquivoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArquivoService {

    private final ArquivoRepository arquivoRepository;

    @Transactional
    public byte[] buscarDadosPorId(Long idArquivo) {
        return arquivoRepository.buscarDadosPorId(idArquivo);
    }

    @Transactional
    public String buscarNomePorId(Long idArquivo) {
        return arquivoRepository.buscarNomePorId(idArquivo);
    }

}

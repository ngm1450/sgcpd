package br.sgcpd.backend.repository;

import br.sgcpd.backend.entity.Arquivo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArquivoRepository extends JpaRepository<Arquivo, Long> {

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO arquivo (nome, tamanho, dados, id_conteudo)
        VALUES (:nome, :tamanho, :dados, :idConteudo)
        """, nativeQuery = true)
    void inserirArquivo(
        @Param("nome") String nome,
        @Param("tamanho") Long tamanho,
        @Param("dados") byte[] dados,
        @Param("idConteudo") Long idConteudo
    );

    @Query(value = "SELECT dados FROM arquivo WHERE id = :id", nativeQuery = true)
    byte[] buscarDadosPorId(@Param("id") Long id);

    @Query(value = "SELECT nome FROM arquivo WHERE id = :id", nativeQuery = true)
    String buscarNomePorId(@Param("id") Long id);

}

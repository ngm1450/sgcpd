package br.sgcpd.backend.entity;

import br.sgcpd.backend.enums.StatusConteudoEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "conteudo")
public class Conteudo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 160)
    private String titulo;

    @Lob
    @Column(nullable = false)
    private String corpo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusConteudoEnum status = StatusConteudoEnum.RASCUNHO;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_autor", foreignKey = @ForeignKey(name = "fk_autor_conteudo"))
    private Usuario autor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", foreignKey = @ForeignKey(name = "fk_categoria_conteudo"))
    private ConteudoCategoria categoria;

    @ManyToMany
    @JoinTable(name = "conteudo_tag",
            joinColumns = @JoinColumn(name = "id_conteudo"),
            inverseJoinColumns = @JoinColumn(name = "id_tag"),
            foreignKey = @ForeignKey(name = "fk_ct_content"),
            inverseForeignKey = @ForeignKey(name = "fk_ct_tag"))
    private Set<ConteudoTag> tags = new HashSet<>();

    @Column(nullable = false, updatable = false)
    private Instant dataCriacao = Instant.now();

    @Column(nullable = false)
    private Instant dataAtualizacao = Instant.now();

    @PreUpdate
    public void onUpdate() { this.dataAtualizacao = Instant.now(); }

}

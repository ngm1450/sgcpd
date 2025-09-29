package br.sgcpd.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "conteudo_categoria", uniqueConstraints = @UniqueConstraint(name = "uk_category_name", columnNames = "name"))
public class ConteudoCategoria {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nome;

    @Column(length = 240)
    private String descricao;

}

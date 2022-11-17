package br.com.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@Entity
@SuperBuilder
@Table(name = "produto")
@EqualsAndHashCode(callSuper = true)
public class Produto extends BaseEntity {

    @EmbeddedId
    private ProdutoId produtoId;
    private String nome;
    @Column(columnDefinition = "integer default -1")
    private Integer limiteAplicacoesAfiliados;
    private Integer moedaAfiliado;
    private Integer moedaPadrinho;
    @Column(columnDefinition = "integer default 3")
    private Integer limiteAplicaoBonusPadrinho;
    private String postWebhockPadrinho;
}

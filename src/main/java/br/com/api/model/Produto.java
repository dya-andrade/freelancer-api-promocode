package br.com.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@Table(name = "produto")
@EqualsAndHashCode(callSuper = true)
public class Produto extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ProdutoID produtoId;
    private String nome;
    @Column(columnDefinition = "integer default -1")
    private Integer limiteAplicacoesAfiliados;
    private Integer moedaAfiliado;
    private Integer moedaPadrinho;
    @Column(columnDefinition = "integer default 3")
    private Integer limiteAplicacaoBonusPadrinho;
    private String postWebhockPadrinho;

}

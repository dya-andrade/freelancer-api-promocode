package br.com.api.model;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class PromoCodeId {

    private Cliente clientePadrinhoId;
    private Produto produtoId;
}

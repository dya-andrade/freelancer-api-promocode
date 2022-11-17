package br.com.api.model;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class ProdutoId {

    private String id;
    private App appId;
}

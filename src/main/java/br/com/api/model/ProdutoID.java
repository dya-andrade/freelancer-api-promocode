package br.com.api.model;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
@Embeddable
public class ProdutoID implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    @ManyToOne
    private App app;

}

package br.com.api.model;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
@Embeddable
public class PromoCodeID implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    private Cliente clientePadrinho;
    @ManyToOne
    private Produto produto;

}

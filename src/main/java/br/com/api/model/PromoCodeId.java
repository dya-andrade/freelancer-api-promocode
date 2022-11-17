package br.com.api.model;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Data
@Embeddable
public class PromoCodeId implements Serializable {

    @ManyToOne
    private Cliente clientePadrinho;
    @OneToOne
    private Produto produto;
}

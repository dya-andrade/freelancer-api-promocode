package br.com.api.model;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Data
@Embeddable
public class EventoAfiliadoId implements Serializable {

    @OneToOne
    private PromoCode promoCode;
    @ManyToOne
    private Cliente clienteAfiliado;
}

package br.com.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class EventoAfiliadoID implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    private PromoCode promoCode;
    @ManyToOne
    private Cliente clienteAfiliado;

}

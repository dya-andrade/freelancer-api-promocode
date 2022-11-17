package br.com.api.model;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class EventoAfiliadoId {

    private PromoCode promoCodeId;
    private Cliente clienteAfiliadoId;
}

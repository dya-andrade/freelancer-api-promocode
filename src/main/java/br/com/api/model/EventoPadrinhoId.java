package br.com.api.model;

import lombok.Data;
import lombok.Generated;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.UUID;

@Data
@Embeddable
public class EventoPadrinhoId {

    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uid;
    private EventoAfiliado eventoAfiliadoId;
}

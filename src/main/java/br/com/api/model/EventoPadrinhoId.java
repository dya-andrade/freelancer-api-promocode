package br.com.api.model;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.UUID;

@Data
@Embeddable
public class EventoPadrinhoId implements Serializable {

    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uid;
    @ManyToOne
    private EventoAfiliado eventoAfiliado;
}

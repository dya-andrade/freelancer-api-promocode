package br.com.api.model;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.UUID;

@Data
@Embeddable
public class EventoPadrinhoID implements Serializable {

    private static final long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uid;
    @ManyToOne
    private EventoAfiliado eventoAfiliado;

}

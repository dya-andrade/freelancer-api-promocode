package br.com.api.model;

import javax.persistence.EmbeddedId;

public class EventoManual {

    @EmbeddedId
    private EventoManualId eventoManualId;
    private Integer moeda;
    private String motivo;
    private String tipo;
    private String idReferencia;
}

package br.com.api.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Builder
@Table(name = "evento_manual")
@EqualsAndHashCode(callSuper = true)
public class EventoManual extends BaseEntity {

    @EmbeddedId
    private EventoManualId eventoManualId;
    private Integer moeda;
    private String motivo;
    private String tipo;
    private String idReferencia;
}

package br.com.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@Table(name = "evento_manual")
@EqualsAndHashCode(callSuper = true)
public class EventoManual extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private EventoManualID eventoManualId;
    private Integer moeda;
    private String motivo;
    @Enumerated(EnumType.STRING)
    private TipoEventoManual tipo;
    private String idReferencia;

}

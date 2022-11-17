package br.com.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@SuperBuilder
@Table(name = "evento_afiliado")
@EqualsAndHashCode(callSuper = true)
public class EventoAfiliado extends BaseEntity {

    @EmbeddedId
    private EventoAfiliadoId eventoAfiliadoId;
    private Integer moeda;
}

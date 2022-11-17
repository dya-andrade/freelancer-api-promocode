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
@Table(name = "evento_padrinho")
@EqualsAndHashCode(callSuper = true)
public class EventoPadrinho extends BaseEntity {

    @EmbeddedId
    private EventoPadrinhoId eventoPadrinhoId;
    private Integer moeda;
}

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
@Table(name = "evento_padrinho")
@EqualsAndHashCode(callSuper = true)
public class EventoPadrinho extends BaseEntity {

    @EmbeddedId
    private EventoPadrinhoId eventoPadrinhoId;
    private Integer moeda;
}

package br.com.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@Table(name = "evento_padrinho")
@EqualsAndHashCode(callSuper = true)
public class EventoPadrinho extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private EventoPadrinhoID eventoPadrinhoId;
    private Integer moeda;

}

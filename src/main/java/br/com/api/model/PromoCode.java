package br.com.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@Table(name = "promo_code")
@EqualsAndHashCode(callSuper = true)
public class PromoCode extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private PromoCodeID promoCodeId;
    @Column(unique = true)
    private String promoCode;
    @Column(columnDefinition = "integer default -1")
    private Integer limiteAplicacoesAfiliados;
}

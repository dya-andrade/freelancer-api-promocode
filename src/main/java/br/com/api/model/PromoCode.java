package br.com.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@SuperBuilder
@Table(name = "promo_code")
@EqualsAndHashCode(callSuper = true)
public class PromoCode extends BaseEntity {

    @EmbeddedId
    private PromoCodeId promoCodeId;
    @Column(unique = true)
    private String promoCode;
    @Column(columnDefinition = "integer default -1")
    private Integer limiteAplicacoesAfiliados;
}

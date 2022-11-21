package br.com.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PromoCodeDTO {

    private String code;
    private Long qtdAfiliados;
    private Long qtdAtivacoes;
    private Long moedas;

}

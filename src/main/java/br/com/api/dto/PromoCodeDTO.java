package br.com.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromoCodeDTO {

    private String code;
    private String produto;
    private Long qtdAfiliados;
    private Long qtdAtivacoes;
    private Long moedas;

}

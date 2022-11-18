package br.com.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class PromoCodeDTO {

    private String code;
    private Long qtdAfiliados;
    private Long qtdAtivacoes;
    private Long moedas;

}

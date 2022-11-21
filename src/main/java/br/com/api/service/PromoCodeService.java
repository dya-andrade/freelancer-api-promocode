package br.com.api.service;

import br.com.api.dto.PromoCodeDTO;
import br.com.api.model.App;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Validated
public interface PromoCodeService {

    PromoCodeDTO criaPromoCode(@NotNull App app, @NotBlank String idCliente, @NotBlank String produtoId);

}

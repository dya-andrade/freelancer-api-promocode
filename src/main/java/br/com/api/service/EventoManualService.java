package br.com.api.service;

import br.com.api.dto.EventoManualDTO;
import br.com.api.dto.RetornoDTO;
import br.com.api.model.App;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Validated
public interface EventoManualService {

    RetornoDTO criaEventoManual(@NotNull App app, @NotBlank String idCliente, @NotNull EventoManualDTO eventoManualDTO);

}

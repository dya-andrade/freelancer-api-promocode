package br.com.api.dto;

import br.com.api.model.TipoEventoManual;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class EventoManualDTO {

    @NotNull
    private Integer moeda;
    @NotNull
    private TipoEventoManual tipo;
    @NotBlank
    private String motivo;
    private String idReferencia;

}

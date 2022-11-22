package br.com.api.dto;

import br.com.api.model.TipoEventoManual;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventoManualDTO {

    @NotNull
    private Integer moeda;
    @NotNull
    private TipoEventoManual tipo;
    @NotBlank
    private String motivo;
    private String idReferencia;

}

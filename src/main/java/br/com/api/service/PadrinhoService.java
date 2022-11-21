package br.com.api.service;

import br.com.api.dto.ClienteDTO;
import br.com.api.dto.PadrinhoSaldoDTO;
import br.com.api.dto.PromoCodeDTO;
import br.com.api.model.App;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface PadrinhoService {

    PadrinhoSaldoDTO consultaSaldo(@NotNull App app, @NotBlank String idCliente);

    List<PromoCodeDTO> consultaDetalhada(@NotNull App app, @NotBlank String idCliente, @NotBlank String dataInicio,
        @NotBlank String dataFim, @NotNull ClienteDTO clientePadrinhoDTO);
}

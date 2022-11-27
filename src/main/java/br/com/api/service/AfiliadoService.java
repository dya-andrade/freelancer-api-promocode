package br.com.api.service;

import br.com.api.dto.AfiliadoDTO;
import br.com.api.dto.AfiliadoSaldoDTO;
import br.com.api.dto.ClienteDTO;
import br.com.api.model.App;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface AfiliadoService {

    AfiliadoSaldoDTO aplicaPromoCode(@NotNull App app, @NotBlank String idCliente, @NotBlank String promocode,
                                     @NotNull ClienteDTO clienteAfiliadoDTO);

    List<AfiliadoDTO> consultaMeusAfiliados(@NotNull App app, @NotBlank String idCliente, @NotBlank String dataInicio, @NotBlank String dataFim);

}

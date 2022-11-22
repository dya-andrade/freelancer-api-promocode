package br.com.api.service;

import br.com.api.dto.ClienteDTO;
import br.com.api.model.App;
import br.com.api.model.Cliente;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Validated
public interface ClienteService {

    Cliente validaClienteExistente(@NotNull App app, @NotBlank String idCliente, @NotNull ClienteDTO clienteDTO);

    Cliente buscaCliente(@NotNull App app, @NotBlank String idCliente);

}

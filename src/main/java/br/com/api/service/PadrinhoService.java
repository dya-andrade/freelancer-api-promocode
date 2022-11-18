package br.com.api.service;

import br.com.api.dto.ClientePadrinhoDTO;
import br.com.api.dto.PadrinhoSaldoDTO;
import br.com.api.dto.PromoCodeDTO;
import br.com.api.exception.ResourceNotFoundException;
import br.com.api.model.App;
import br.com.api.model.Cliente;
import br.com.api.model.ClienteID;
import br.com.api.repository.ClienteRepository;
import br.com.api.repository.EventoPadrinhoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Service
@Validated
@RequiredArgsConstructor
public class PadrinhoService {

    private final ClienteRepository clienteRepository;

    private final EventoPadrinhoRepository eventoPadrinhoRepository;

    private static ClienteID buildClienteID(final App app, final String idCliente) {
        return ClienteID.builder()
            .id(idCliente)
            .app(app)
            .build();
    }

    public PadrinhoSaldoDTO consultaSaldo(@NotNull App app, @NotBlank String idCliente) {

        var cliente = clienteRepository.findById(buildClienteID(app, idCliente))
            .orElseThrow(() -> new ResourceNotFoundException("Erro ao tentar buscar cliente, ID ou App inválidos."));

        var padrinhoSaldoDTO = eventoPadrinhoRepository.findPadrinhoSaldo(cliente);

        return padrinhoSaldoDTO;
    }

    public List<PromoCodeDTO> consultaDetalhes(@NotNull App app, @NotBlank String idCliente, @NotNull Date dataInicio,
        @NotNull Date dataFim, @NotNull ClientePadrinhoDTO clientePadrinhoDTO) {

        var clientePadrinho = clienteRepository.findById(buildClienteID(app, idCliente))
            .orElse(clienteRepository.save(
                Cliente.builder()
                    .clienteId(buildClienteID(app, idCliente))
                    .nome(clientePadrinhoDTO.getNome())
                    .email(clientePadrinhoDTO.getEmail())
                    .dtCriacao(LocalDateTime.now())
                    .build()));

        var promoCodes = eventoPadrinhoRepository.findPromoCodes(clientePadrinho, dataInicio, dataFim);

        return promoCodes;
    }

}

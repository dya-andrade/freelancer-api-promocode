package br.com.api.service.impl;

import br.com.api.dto.ClienteDTO;
import br.com.api.dto.PadrinhoSaldoDTO;
import br.com.api.dto.PromoCodeDTO;
import br.com.api.model.App;
import br.com.api.repository.EventoPadrinhoRepository;
import br.com.api.repository.PromoCodeRepository;
import br.com.api.service.PadrinhoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class PadrinhoServiceImpl implements PadrinhoService {

    private final ClienteServiceImpl clienteService;

    private final EventoPadrinhoRepository eventoPadrinhoRepository;

    private final PromoCodeRepository promoCodeRepository;


    @Override
    public PadrinhoSaldoDTO consultaSaldo(final App app, final String idCliente) {

        log.info("SERVICE: CONSULTA SALDO");

        log.info("Busca cliente.");

        var clientePadrinho = clienteService.buscaCliente(app, idCliente);

        log.info("Consutal saldo.");

        var saldoTotal = 0L;

        var saldoPadrinho = eventoPadrinhoRepository.consultaSaldoPadrinho(clientePadrinho);

        if (nonNull(saldoPadrinho))
            saldoTotal += saldoPadrinho;

        return new PadrinhoSaldoDTO(saldoTotal);
    }

    @Override
    public List<PromoCodeDTO> consultaDetalhada(final App app, final String idCliente, final String dataInicio, final String dataFim,
        final ClienteDTO clientePadrinhoDTO) {

        log.info("SERVICE: CONSULTA DETALHADA");

        log.info("Busca cliente.");

        var clientePadrinho = clienteService.validaClienteExistente(app, idCliente, clientePadrinhoDTO);

        log.info("Busca promocodes.");

        var promoCodesPadrinho = promoCodeRepository.findByPromoCodeIdClientePadrinho(clientePadrinho);

        log.info("Converte as datas.");

        DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd")
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0).toFormatter();

        log.info("Consulta detalhada dos promocodes.");

        return promoCodesPadrinho.stream().map(promoCode -> eventoPadrinhoRepository
                .consultaEventosPromoCodePadrinho(promoCode, LocalDateTime.parse(dataInicio, formatter), LocalDateTime.parse(dataFim, formatter))
                .orElse(PromoCodeDTO.builder()
                    .code(promoCode.getPromoCode())
                    .qtdAfiliados(0L)
                    .qtdAtivacoes(0L)
                    .moedas(0L)
                    .build()))
            .collect(Collectors.toList());
    }

}

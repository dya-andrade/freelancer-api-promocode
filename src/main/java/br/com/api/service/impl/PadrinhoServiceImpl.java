package br.com.api.service.impl;

import br.com.api.dto.*;
import br.com.api.model.App;
import br.com.api.model.EventoPadrinho;
import br.com.api.model.EventoPadrinhoID;
import br.com.api.repository.EventoAfiliadoRepository;
import br.com.api.repository.EventoManualRepository;
import br.com.api.repository.EventoPadrinhoRepository;
import br.com.api.repository.PromoCodeRepository;
import br.com.api.service.PadrinhoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

import static br.com.api.util.DateFormatter.dateFormatter;
import static java.util.Objects.nonNull;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class PadrinhoServiceImpl implements PadrinhoService {

    private final ClienteServiceImpl clienteService;
    private final EventoPadrinhoRepository eventoPadrinhoRepository;
    private final EventoAfiliadoRepository eventoAfiliadoRepository;
    private final EventoManualRepository eventoManualRepository;
    private final PromoCodeRepository promoCodeRepository;

    private static RetornoDTO buildRetornoDTO(Boolean ok) {
        return RetornoDTO.
                builder()
                .ok(ok)
                .build();
    }

    @Override
    public PadrinhoSaldoDTO consultaSaldo(final App app, final String idCliente) {

        log.info("SERVICE: CONSULTA SALDO");

        log.info("Busca cliente.");

        var clientePadrinho = clienteService.buscaCliente(app, idCliente);

        log.info("Consulta saldo.");

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

        log.info("Consulta detalhada dos promocodes.");

        return promoCodesPadrinho.stream().map(promoCode -> eventoPadrinhoRepository
                .consultaEventosPromoCode(promoCode, dateFormatter(dataInicio), dateFormatter(dataFim))
                .orElse(PromoCodeDTO.builder()
                        .code(promoCode.getPromoCode())
                        .produto(promoCode.getPromoCodeId()
                                .getProduto().getNome())
                        .qtdAfiliados(0L)
                        .qtdAtivacoes(0L)
                        .moedas(0L)
                        .build())).toList();
    }

    private String geraUIDEventoPadrinho() {
        var uid = UUID.randomUUID().toString();

        var eventoPadrinhoOptional = eventoPadrinhoRepository
                .findByEventoPadrinhoIdUid(uid);

        if (eventoPadrinhoOptional.isPresent())
            return geraUIDEventoPadrinho();

        return uid;
    }

    @Override
    public RetornoDTO aplicaPadrinho(final App app, final String idCliente, final String idReferencia) {

        log.info("SERVICE: APLICA PADRINHO");

        log.info("Busca cliente.");

        var clienteAfiliado = clienteService.buscaCliente(app, idCliente);

        log.info("Busca eventos afiliado.");

        var eventosAfiliado = eventoAfiliadoRepository.consultaEventosAfiliado(clienteAfiliado);

        var retornoDTO = buildRetornoDTO(false);

        eventosAfiliado.forEach(eventoAfiliado -> {

            log.info("Verifica duplicidade evento padrinho.");

            var eventoPadrinhoOptional = eventoPadrinhoRepository
                    .findByEventoPadrinhoIdEventoAfiliadoAndIdReferencia(eventoAfiliado, idReferencia);

            if (eventoPadrinhoOptional.isEmpty()) {

                var promoCode = eventoAfiliado.getEventoAfiliadoId().getPromoCode();
                var produto = promoCode.getPromoCodeId().getProduto();

                log.info("Verifica as aplicações de bonus padrinho.");

                var qtAplicacoesPadrinho = eventoPadrinhoRepository.consultaQtdAplicacoesPromoCode(promoCode);
                var limiteAplicacaoBonusPadrinho = produto.getLimiteAplicacaoBonusPadrinho();

                if (limiteAplicacaoBonusPadrinho > 0 && qtAplicacoesPadrinho < limiteAplicacaoBonusPadrinho) {
                    eventoPadrinhoRepository.save(
                            EventoPadrinho.builder()
                                    .eventoPadrinhoId(
                                            EventoPadrinhoID.builder()
                                                    .uid(geraUIDEventoPadrinho())
                                                    .eventoAfiliado(eventoAfiliado)
                                                    .build())
                                    .moeda(produto.getMoedaPadrinho())
                                    .idReferencia(idReferencia)
                                    .dtCriacao(LocalDateTime.now())
                                    .build());

                    retornoDTO.setOk(true);
                }
            }
        });

        return retornoDTO;
    }

    @Override
    public List<ExtratoDTO> consultaExtrato(final App app, final String idCliente, final String dataInicio, final String dataFim) {

        log.info("SERVICE: CONSULTA EXTRATO");

        log.info("Busca cliente.");

        var cliente = clienteService.buscaCliente(app, idCliente);

        List<ExtratoDTO> extratosDTO = new ArrayList<>();

        log.info("Consulta extrato eventos padrinho.");

        extratosDTO.addAll(eventoPadrinhoRepository
                .consultaExtratoEventosPadrinho(cliente, dateFormatter(dataInicio), dateFormatter(dataFim)));

        log.info("Consulta extrato eventos afiliado.");

        extratosDTO.addAll(eventoAfiliadoRepository
                .consultaExtratoEventosAfiliado(cliente, dateFormatter(dataInicio), dateFormatter(dataFim)));

        log.info("Consulta extrato eventos manuais.");

        extratosDTO.addAll(eventoManualRepository.consultaExtratoEventosManuais(cliente, dateFormatter(dataInicio), dateFormatter(dataFim)));

        Collections.sort(extratosDTO, Comparator.comparing(ExtratoDTO::getDtInclusao));

        return extratosDTO;
    }

}

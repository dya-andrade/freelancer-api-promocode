package br.com.api.service.impl;

import br.com.api.dto.AfiliadoSaldoDTO;
import br.com.api.dto.ClienteDTO;
import br.com.api.dto.RetornoDTO;
import br.com.api.exception.ResourceConflictException;
import br.com.api.exception.ResourceNotFoundException;
import br.com.api.model.*;
import br.com.api.repository.EventoAfiliadoRepository;
import br.com.api.repository.EventoPadrinhoRepository;
import br.com.api.repository.PromoCodeRepository;
import br.com.api.service.AfiliadoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

import static br.com.api.exception.util.MessageException.*;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class AfiliadoServiceImpl implements AfiliadoService {

    private final PromoCodeRepository promoCodeRepository;

    private final EventoPadrinhoRepository eventoPadrinhoRepository;

    private final EventoAfiliadoRepository eventoAfiliadoRepository;

    private final ClienteServiceImpl clienteService;

    private String geraUIDEventoPadrinho() {
        var uid = UUID.randomUUID().toString();

        var eventoPadrinhoOptional = eventoPadrinhoRepository
                .findByEventoPadrinhoIdUid(uid);

        if (eventoPadrinhoOptional.isPresent())
            return geraUIDEventoPadrinho();

        return uid;
    }

    private static RetornoDTO buildRetornoDTO(Boolean ok) {
        return RetornoDTO.
                builder()
                .ok(ok)
                .build();
    }

    @Override
    public AfiliadoSaldoDTO aplicaPromoCode(final App app, final String idCliente, final String promocode, final ClienteDTO clienteAfiliadoDTO) {

        log.info("SERVICE: APLICA PROMOCODE");

        log.info("Busca promocode: " + promocode);

        var promoCode = promoCodeRepository.findByPromoCode(promocode)
                .orElseThrow(() -> new ResourceNotFoundException(PROMOCODE_NAO_ENCONTRADO));

        log.info("Busca cliente.");

        var clienteAfiliado = clienteService.validaClienteExistente(app, idCliente, clienteAfiliadoDTO);

        log.info("Verifica duplicidade aplicação promocode.");

        var eventoAfiliadoID = EventoAfiliadoID.builder()
                .clienteAfiliado(clienteAfiliado)
                .promoCode(promoCode)
                .build();

        var eventoAfiliadoOptional = eventoAfiliadoRepository.findById(eventoAfiliadoID);

        if (eventoAfiliadoOptional.isPresent()) {
            throw new ResourceConflictException(PROMOCODE_JA_APLICADO);
        }

        log.info("Verifica se é padrinho do promocode.");

        var padrinhoPromoCode = promoCode.getPromoCodeId().getClientePadrinho();

        if (clienteAfiliado.getClienteId().equals(padrinhoPromoCode.getClienteId())) {
            throw new ResourceConflictException(PROMOCODE_APLICACAO_PADRINHO);
        }

        log.info("Verifica as aplicações do promocode.");

        var qtdAplicacoesAfiliado = eventoAfiliadoRepository.consultaQtdAplicacoesPromoCode(promoCode);

        var limiteAplicacoesAfiliados = promoCode.getLimiteAplicacoesAfiliados();

        if (limiteAplicacoesAfiliados > 0 && qtdAplicacoesAfiliado < limiteAplicacoesAfiliados) {

            var produto = promoCode.getPromoCodeId().getProduto();

            log.info("Cria evento afiliado.");

            var eventoAfiliado = eventoAfiliadoRepository.save(
                    EventoAfiliado.builder()
                            .eventoAfiliadoId(EventoAfiliadoID.builder()
                                    .promoCode(promoCode)
                                    .clienteAfiliado(clienteAfiliado)
                                    .build())
                            .moeda(produto.getMoedaAfiliado())
                            .dtCriacao(LocalDateTime.now())
                            .build());

            return new AfiliadoSaldoDTO(produto.getMoedaAfiliado());

        } else {
            throw new ResourceConflictException(PROMOCODE_EXPIRADO);
        }
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

}

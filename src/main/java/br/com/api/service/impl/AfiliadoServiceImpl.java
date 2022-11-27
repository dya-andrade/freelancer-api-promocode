package br.com.api.service.impl;

import br.com.api.dto.AfiliadoDTO;
import br.com.api.dto.AfiliadoSaldoDTO;
import br.com.api.dto.ClienteDTO;
import br.com.api.exception.ResourceConflictException;
import br.com.api.exception.ResourceNotFoundException;
import br.com.api.model.App;
import br.com.api.model.EventoAfiliado;
import br.com.api.model.EventoAfiliadoID;
import br.com.api.repository.EventoAfiliadoRepository;
import br.com.api.repository.PromoCodeRepository;
import br.com.api.service.AfiliadoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static br.com.api.exception.util.MessageError.*;
import static br.com.api.util.DateFormatter.dateFormatter;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class AfiliadoServiceImpl implements AfiliadoService {

    private final PromoCodeRepository promoCodeRepository;
    private final EventoAfiliadoRepository eventoAfiliadoRepository;
    private final ClienteServiceImpl clienteService;


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

            eventoAfiliadoRepository.save(
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
    public List<AfiliadoDTO> consultaMeusAfiliados(App app, String idCliente, String dataInicio, String dataFim) {

        log.info("SERVICE: CONSULTA MEUS AFILIADOS");

        log.info("Busca cliente.");

        var clientePadrinho = clienteService.buscaCliente(app, idCliente);

        log.info("Consulta afiliados do padrinho.");

        return eventoAfiliadoRepository.consultaMeusAfiliados(clientePadrinho, dateFormatter(dataInicio), dateFormatter(dataFim));
    }

}

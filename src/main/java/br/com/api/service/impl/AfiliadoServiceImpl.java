package br.com.api.service.impl;

import br.com.api.dto.AfiliadoSaldoDTO;
import br.com.api.dto.ClienteDTO;
import br.com.api.exception.ResourceConflictException;
import br.com.api.exception.ResourceNotFoundException;
import br.com.api.model.App;
import br.com.api.model.EventoAfiliado;
import br.com.api.model.EventoAfiliadoID;
import br.com.api.model.EventoPadrinho;
import br.com.api.model.EventoPadrinhoID;
import br.com.api.repository.EventoAfiliadoRepository;
import br.com.api.repository.EventoPadrinhoRepository;
import br.com.api.repository.ProdutoRepository;
import br.com.api.repository.PromoCodeRepository;
import br.com.api.service.AfiliadoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

import static br.com.api.exception.util.MessageException.PROMOCODE_APLICACAO_PADRINHO;
import static br.com.api.exception.util.MessageException.PROMOCODE_EXPIRADO;
import static br.com.api.exception.util.MessageException.PROMOCODE_JA_APLICADO;
import static br.com.api.exception.util.MessageException.PROMOCODE_NAO_ENCONTRADO;

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

    @Override
    public AfiliadoSaldoDTO aplicaPromoCode(final App app, final String idCliente, final String promocode, final ClienteDTO clienteAfiliadoDTO) {

        log.info("SERVICE: APLICA PROMOCODE");

        log.info("Busca promocode: " + promocode);

        var promoCode = promoCodeRepository.findByPromoCode(promocode)
            .orElseThrow(() -> new ResourceNotFoundException(PROMOCODE_NAO_ENCONTRADO));

        log.info("Busca cliente." );

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

        if (promoCode.getLimiteAplicacoesAfiliados() > 0 && qtdAplicacoesAfiliado < promoCode.getLimiteAplicacoesAfiliados()) {

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

}

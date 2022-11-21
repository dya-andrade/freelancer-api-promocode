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
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

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

        var promoCode = promoCodeRepository.findByPromoCode(promocode)
            .orElseThrow(() -> new ResourceNotFoundException("Erro ao tentar buscar promocode, promocode não encontrado."));

        var clienteAfiliado = clienteService.validaClienteExistente(app, idCliente, clienteAfiliadoDTO);

        var eventoAfiliadoID = EventoAfiliadoID.builder()
            .clienteAfiliado(clienteAfiliado)
            .promoCode(promoCode)
            .build();

        var eventoAfiliadoOptional = eventoAfiliadoRepository.findById(eventoAfiliadoID);

        if (eventoAfiliadoOptional.isPresent()) {
            throw new ResourceConflictException("Erro ao tentar aplicar promocode, promocode já foi aplicado.");
        }

        var padrinhoPromoCode = promoCode.getPromoCodeId().getClientePadrinho();

        if(clienteAfiliado.getClienteId().equals(padrinhoPromoCode.getClienteId())){
            throw new ResourceConflictException("Erro ao tentar aplicar promocode, cliente é padrinho do promocode.");
        }

        var qtdAplicacoesAfiliado = eventoAfiliadoRepository.consultaQtdAplicacoesPromoCode(promoCode);

        if (promoCode.getLimiteAplicacoesAfiliados() > 0 && qtdAplicacoesAfiliado < promoCode.getLimiteAplicacoesAfiliados()) {

            var produto = promoCode.getPromoCodeId().getProduto();

            var eventoAfiliado = eventoAfiliadoRepository.save(
                EventoAfiliado.builder()
                    .eventoAfiliadoId(EventoAfiliadoID.builder()
                        .promoCode(promoCode)
                        .clienteAfiliado(clienteAfiliado)
                        .build())
                    .moeda(produto.getMoedaAfiliado())
                    .dtCriacao(LocalDateTime.now())
                    .build());

                eventoPadrinhoRepository.save(
                    EventoPadrinho.builder()
                        .eventoPadrinhoId(EventoPadrinhoID.builder()
                            .uid(geraUIDEventoPadrinho())
                            .eventoAfiliado(eventoAfiliado)
                            .build())
                        .moeda(produto.getMoedaPadrinho())
                        .dtCriacao(LocalDateTime.now())
                        .build());

            return new AfiliadoSaldoDTO(produto.getMoedaAfiliado());
        } else {
            throw new ResourceConflictException("Erro ao tentar aplicar promocode, promocode está expirado.");
        }
    }
}

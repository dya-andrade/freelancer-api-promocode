package br.com.api.service.impl;

import br.com.api.dto.PromoCodeDTO;
import br.com.api.exception.ResourceNotFoundException;
import br.com.api.model.App;
import br.com.api.model.ProdutoID;
import br.com.api.model.PromoCode;
import br.com.api.model.PromoCodeID;
import br.com.api.repository.EventoPadrinhoRepository;
import br.com.api.repository.ProdutoRepository;
import br.com.api.repository.PromoCodeRepository;
import br.com.api.service.PromoCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class PromoCodeServiceImpl implements PromoCodeService {

    private final EventoPadrinhoRepository eventoPadrinhoRepository;

    private final PromoCodeRepository promoCodeRepository;

    private final ProdutoRepository produtoRepository;

    private final ClienteServiceImpl clienteService;


    private String geraPromoCode() {
        var code = RandomStringUtils.randomAlphanumeric(7).toUpperCase();

        var promoCode = promoCodeRepository.findByPromoCode(code);

        if (promoCode.isPresent())
            return geraPromoCode();

        return code;
    }

    @Override
    public PromoCodeDTO criaPromoCode(final App app, final String idCliente, final String produtoId) {

        var buildProdutoId = ProdutoID.builder()
            .id(produtoId)
            .app(app)
            .build();

        var produto = produtoRepository.findById(buildProdutoId)
            .orElseThrow(() -> new ResourceNotFoundException("Erro ao tentar buscar produto, ID não encontrado."));

        var clientePadrinho = clienteService.buscaCliente(app, idCliente);

        var buildPromoCodeId = PromoCodeID.builder()
            .clientePadrinho(clientePadrinho)
            .produto(produto)
            .build();

        var promoCodeOptional = promoCodeRepository.findById(buildPromoCodeId);
        PromoCode promoCode = null;

        if (promoCodeOptional.isEmpty()) {
            promoCode = promoCodeRepository.save(
                PromoCode.builder()
                    .promoCodeId(buildPromoCodeId)
                    .promoCode(geraPromoCode())
                    .limiteAplicacoesAfiliados(produto.getLimiteAplicacoesAfiliados())
                    .dtCriacao(LocalDateTime.now())
                    .build());
        } else {
            promoCode = promoCodeOptional.get();
        }

        return eventoPadrinhoRepository.consultaEventosPromoCode(promoCode)
            .orElse(PromoCodeDTO.builder()
                .code(promoCode.getPromoCode())
                .qtdAfiliados(0L)
                .qtdAtivacoes(0L)
                .moedas(0L)
                .build());
    }

}

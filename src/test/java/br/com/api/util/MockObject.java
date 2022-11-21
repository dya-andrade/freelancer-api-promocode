package br.com.api.util;

import br.com.api.dto.ClienteDTO;
import br.com.api.model.App;
import br.com.api.model.Cliente;
import br.com.api.model.ClienteID;
import br.com.api.model.EventoAfiliado;
import br.com.api.model.EventoAfiliadoID;
import br.com.api.model.EventoManual;
import br.com.api.model.EventoManualID;
import br.com.api.model.EventoPadrinho;
import br.com.api.model.EventoPadrinhoID;
import br.com.api.model.Produto;
import br.com.api.model.ProdutoID;
import br.com.api.model.PromoCode;
import br.com.api.model.PromoCodeID;

import java.time.LocalDateTime;

import static br.com.api.model.TipoEventoManual.RET;

public class MockObject {

    public static String PROMO_CODE = "AQDC3BO";

    public static String ID_CLIENTE_PADRINHO = "4581-97c7";

    public static String ID_CLIENTE_AFILIADO = "ff0c-4581";

    public static String TOKEN = "q4YZ$ys5etJMrmnV";

    public static final int SERVER_PORT = 8888;

    public static final String HEADER_PARAM_AUTHORIZATION = "token";

    public static final String CONTENT_TYPE_JSON = "application/json";

    public static ClienteDTO clienteAfiliadoDTO(){
        return ClienteDTO.builder()
            .nome(clienteAfiliado().getNome())
            .email(clienteAfiliado().getEmail())
            .build();
    }

    public static ClienteDTO clienteAfiliadoDTO2(){
        return ClienteDTO.builder()
            .nome(clienteAfiliado2().getNome())
            .email(clienteAfiliado2().getEmail())
            .build();
    }

    public static ClienteDTO clientePadrinhoDTO(){
        return ClienteDTO.builder()
            .nome(clientePadrinho().getNome())
            .email(clientePadrinho().getEmail())
            .build();
    }

    public static PromoCodeID promoCodeID(){
        return PromoCodeID.builder()
            .produto(produto())
            .clientePadrinho(clientePadrinho())
            .build();
    }

    public static PromoCode promoCode(){
        return PromoCode.builder()
            .promoCodeId(promoCodeID())
            .promoCode("AQDC3BO")
            .limiteAplicacoesAfiliados(
                promoCodeID().getProduto()
                    .getLimiteAplicacoesAfiliados())
            .dtCriacao(LocalDateTime.now())
            .build();
    }

    public static ProdutoID produtoID(){
        return ProdutoID.builder()
            .id("ff0c-4581-97c7")
            .app(app())
            .build();
    }

    public static Produto produto(){
        return Produto.builder()
            .produtoId(produtoID())
            .nome("Black Friday")
            .limiteAplicacoesAfiliados(15)
            .limiteAplicacaoBonusPadrinho(3)
            .moedaPadrinho(2)
            .moedaAfiliado(20)
            .postWebhockPadrinho("Promoção")
            .dtCriacao(LocalDateTime.now())
            .build();
    }

    public static App app(){
        return App.builder()
            .uid("26ed47e2-281eeab5e652")
            .nome("Tua Agenda")
            .token("q4YZ$ys5etJMrmnV")
            .dtCriacao(LocalDateTime.now())
            .build();
    }

    public static ClienteID clientePadrinhoID(){
        return ClienteID.builder()
            .id("4581-97c7")
            .app(app())
            .build();
    }

    public static Cliente clientePadrinho(){
        return Cliente.builder()
            .clienteId(clientePadrinhoID())
            .nome("Maria")
            .email("maria@gmail.com")
            .dtCriacao(LocalDateTime.now())
            .build();
    }

    public static ClienteID clienteAfiliadoID(){
        return ClienteID.builder()
            .id("ff0c-4581")
            .app(app())
            .build();
    }

    public static ClienteID clienteAfiliadoID2(){
        return ClienteID.builder()
            .id("ff1c-5842")
            .app(app())
            .build();
    }

    public static Cliente clienteAfiliado(){
        return Cliente.builder()
            .clienteId(clienteAfiliadoID())
            .nome("Joana")
            .email("joana@gmail.com")
            .dtCriacao(LocalDateTime.now())
            .build();
    }

    public static Cliente clienteAfiliado2(){
        return Cliente.builder()
            .clienteId(clienteAfiliadoID2())
            .nome("Matheus")
            .email("Matheus@gmail.com")
            .dtCriacao(LocalDateTime.now())
            .build();
    }

    public static EventoAfiliadoID eventoAfiliadoID(){
        return EventoAfiliadoID.builder()
            .promoCode(promoCode())
            .clienteAfiliado(clienteAfiliado())
            .build();
    }

    public static EventoAfiliado eventoAfiliado(){
        return EventoAfiliado.builder()
            .eventoAfiliadoId(eventoAfiliadoID())
            .moeda(eventoAfiliadoID().getPromoCode().getPromoCodeId()
                .getProduto().getMoedaAfiliado())
            .dtCriacao(LocalDateTime.now())
            .build();
    }

    public static EventoPadrinhoID eventoPadrinhoID(){
        return EventoPadrinhoID.builder()
            .uid("4299-ab0c")
            .eventoAfiliado(eventoAfiliado())
            .build();
    }

    public static EventoPadrinho eventoPadrinho(){
        return EventoPadrinho.builder()
            .eventoPadrinhoId(eventoPadrinhoID())
            .moeda(eventoPadrinhoID().getEventoAfiliado().getEventoAfiliadoId()
                .getPromoCode().getPromoCodeId().getProduto().getMoedaPadrinho())
            .dtCriacao(LocalDateTime.now())
            .build();
    }

    public static EventoManualID eventoManualID(){
        return EventoManualID.builder()
            .uid("418e-8c65")
            .cliente(clientePadrinho())
            .build();
    }

    public static EventoManual eventoManual(){
        return EventoManual.builder()
            .eventoManualId(eventoManualID())
            .moeda(-1)
            .motivo("Para ser aplicado em assinatura")
            .tipo(RET)
            .idReferencia("")
            .dtCriacao(LocalDateTime.now())
            .build();
    }
}

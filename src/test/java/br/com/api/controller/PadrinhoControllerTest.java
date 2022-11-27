package br.com.api.controller;

import br.com.api.AbstractIntegration;
import br.com.api.dto.*;
import br.com.api.exception.model.ExceptionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static br.com.api.exception.util.MessageError.EVENTO_SALDO_INSUFICIENTE;
import static br.com.api.util.MockObject.*;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PadrinhoControllerTest extends AbstractIntegration {

    private static ObjectMapper objectMapper;

    private static PromoCodeDTO promoCodeDTO;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    private static RequestSpecification specification(String url) {
        return new RequestSpecBuilder()
                .addHeader(HEADER_PARAM_AUTHORIZATION, TOKEN)
                .setBasePath("/{uidApp}/padrinho" + url)
                .setPort(SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(1)
    @DisplayName("Cria evento manual de ADD")
    void criaEventoManualComSucessoRetornaOk() throws JsonProcessingException {

        var content =
                given()
                        .spec(specification("/{idCliente}/evento-manual"))
                        .contentType(CONTENT_TYPE_JSON)
                        .pathParam("uidApp", app().getUid())
                        .pathParam("idCliente", clientePadrinho().getClienteId().getId())
                        .body(eventoManualDTOADD())
                        .when()
                        .post()
                        .then()
                        .statusCode(201)
                        .extract()
                        .body().asString();

        var retornoDTO = objectMapper.readValue(content, RetornoDTO.class);

        assertNotNull(retornoDTO);

        assertTrue(retornoDTO.getOk());
    }

    @Test
    @Order(2)
    @DisplayName("Cria evento manual de ADD duplicado")
    void criaEventoManualDuplicadoERetornaFalse() throws JsonProcessingException {

        var content =
                given()
                        .spec(specification("/{idCliente}/evento-manual"))
                        .contentType(CONTENT_TYPE_JSON)
                        .pathParam("uidApp", app().getUid())
                        .pathParam("idCliente", clientePadrinho().getClienteId().getId())
                        .body(eventoManualDTOADD())
                        .when()
                        .post()
                        .then()
                        .statusCode(201)
                        .extract()
                        .body().asString();

        var retornoDTO = objectMapper.readValue(content, RetornoDTO.class);

        assertNotNull(retornoDTO);

        assertFalse(retornoDTO.getOk());
    }

    @Test
    @Order(3)
    @DisplayName("Consulta detalhada padrinho")
    void consultaDetalhadaPadrinhoComSucessoRetornaAplicacoes() throws JsonProcessingException {

        var content =
                given()
                        .spec(specification("/detalhes/{idCliente}"))
                        .contentType(CONTENT_TYPE_JSON)
                        .pathParam("uidApp", app().getUid())
                        .pathParam("idCliente", clientePadrinho().getClienteId().getId())
                        .queryParams("dataInicio", "2022-11-20", "dataFim", "2022-11-23")
                        .body(clientePadrinhoDTO())
                        .when()
                        .post()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body().asString();

        var promoCodesDTO = objectMapper.readValue(content, new TypeReference<List<PromoCodeDTO>>() {
        });

        assertNotNull(promoCodesDTO);

        promoCodeDTO = promoCodesDTO.get(0);

        assertEquals(promoCodeDTO.getCode(), PROMO_CODE);
        assertEquals(promoCodeDTO.getProduto(), produto().getNome());
        assertTrue(promoCodeDTO.getQtdAfiliados() >= 0);
        assertTrue(promoCodeDTO.getQtdAtivacoes() >= 0);
        assertTrue(promoCodeDTO.getMoedas() >= 0);
    }

    @Test
    @Order(4)
    @DisplayName("Consulta saldo padrinho")
    void consultaSaldoPadrinhoComSucessoRetornaMoedas() throws JsonProcessingException {

        var content =
                given()
                        .spec(specification("/saldo/{idCliente}"))
                        .contentType(CONTENT_TYPE_JSON)
                        .pathParam("uidApp", app().getUid())
                        .pathParam("idCliente", clientePadrinho().getClienteId().getId())
                        .when()
                        .get()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body().asString();

        var padrinhoSaldoDTO = objectMapper.readValue(content, PadrinhoSaldoDTO.class);

        assertNotNull(padrinhoSaldoDTO);

        assertTrue(padrinhoSaldoDTO.getSaldoAtual().intValue() >= promoCodeDTO.getMoedas() + eventoManualADD().getMoeda());
    }

    @Test
    @Order(5)
    @DisplayName("Consulta detalhada afiliado")
    void consultaDetalhadaAfiliadoComSucessoRetornaAplicacoesVazias() throws JsonProcessingException {

        var content =
                given()
                        .spec(specification("/detalhes/{idCliente}"))
                        .contentType(CONTENT_TYPE_JSON)
                        .pathParam("uidApp", app().getUid())
                        .pathParam("idCliente", clienteAfiliado2().getClienteId().getId())
                        .queryParams("dataInicio", "2022-11-20", "dataFim", "2022-11-23")
                        .body(clienteAfiliadoDTO2())
                        .when()
                        .post()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body().asString();

        var promoCodesDTO = objectMapper.readValue(content, new TypeReference<List<PromoCodeDTO>>() {
        });

        assertNotNull(promoCodesDTO);
        assertTrue(promoCodesDTO.isEmpty());
    }

    @Test
    @Order(6)
    @DisplayName("Cria promocode já existente")
    void criaPromoCodeExistenteERetornaPromoCodeExistente() throws JsonProcessingException {

        var content =
                given()
                        .spec(specification("/{idCliente}/promocode/{produtoId}"))
                        .contentType(CONTENT_TYPE_JSON)
                        .pathParam("uidApp", app().getUid())
                        .pathParam("idCliente", clientePadrinho().getClienteId().getId())
                        .pathParam("produtoId", produto().getProdutoId().getId())
                        .when()
                        .put()
                        .then()
                        .statusCode(201)
                        .extract()
                        .body().asString();

        var promoCodeDTO = objectMapper.readValue(content, PromoCodeDTO.class);

        assertNotNull(promoCodeDTO);
        assertEquals(promoCodeDTO.getCode(), PROMO_CODE);
        assertEquals(promoCodeDTO.getProduto(), produto().getNome());
        assertTrue(promoCodeDTO.getQtdAfiliados() >= 0);
        assertTrue(promoCodeDTO.getQtdAtivacoes() >= 0);
        assertTrue(promoCodeDTO.getMoedas() >= 0);
    }

    @Test
    @Order(7)
    @DisplayName("Cria promocode novo")
    void criaPromoCodeNovoERetornaPromoCode() throws JsonProcessingException {

        var content =
                given()
                        .spec(specification("/{idCliente}/promocode/{produtoId}"))
                        .contentType(CONTENT_TYPE_JSON)
                        .pathParam("uidApp", app().getUid())
                        .pathParam("idCliente", clienteAfiliado2().getClienteId().getId())
                        .pathParam("produtoId", produto().getProdutoId().getId())
                        .when()
                        .put()
                        .then()
                        .statusCode(201)
                        .extract()
                        .body().asString();

        promoCodeDTO = objectMapper.readValue(content, PromoCodeDTO.class);

        assertNotNull(promoCodeDTO);
        assertFalse(promoCodeDTO.getCode().isBlank());
        assertEquals(promoCodeDTO.getProduto(), produto().getNome());
        assertTrue(promoCodeDTO.getQtdAfiliados() >= 0);
        assertTrue(promoCodeDTO.getQtdAtivacoes() >= 0);
        assertTrue(promoCodeDTO.getMoedas() >= 0);
    }

    @Test
    @Order(8)
    @DisplayName("Retorna moedas aplicação")
    void aplicaPromoCodeAfiliadoComSucessoRetornaMoedas() throws JsonProcessingException {

        var specification = new RequestSpecBuilder()
                .addHeader(HEADER_PARAM_AUTHORIZATION, TOKEN)
                .setBasePath("/{uidApp}/afiliado/{idCliente}/aplicar/{promocode}")
                .setPort(SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content =
                given()
                        .spec(specification)
                        .contentType(CONTENT_TYPE_JSON)
                        .pathParam("uidApp", app().getUid())
                        .pathParam("idCliente", clienteAfiliado().getClienteId().getId())
                        .pathParam("promocode", promoCodeDTO.getCode())
                        .body(clienteAfiliadoDTO())
                        .when()
                        .post()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body().asString();

        var afiliadoSaldoDTO = objectMapper.readValue(content, AfiliadoSaldoDTO.class);

        assertNotNull(afiliadoSaldoDTO);

        assertEquals(afiliadoSaldoDTO.getMoedas(), produto().getMoedaAfiliado());
    }

    @Test
    @Order(9)
    @DisplayName("Cria evento manual de RET com saldo insuficiente")
    void criaEventoManualSaldoInsuficienteComRetornaErro() throws JsonProcessingException {

        var content =
                given()
                        .spec(specification("/{idCliente}/evento-manual"))
                        .contentType(CONTENT_TYPE_JSON)
                        .pathParam("uidApp", app().getUid())
                        .pathParam("idCliente", clienteAfiliado().getClienteId().getId())
                        .body(eventoManualDTORET())
                        .when()
                        .post()
                        .then()
                        .statusCode(409)
                        .extract()
                        .body().asString();

        var exceptionResponse = objectMapper.readValue(content, ExceptionResponse.class);

        assertNotNull(exceptionResponse);

        assertEquals(EVENTO_SALDO_INSUFICIENTE, exceptionResponse.getMessage());
    }

    @Test
    @Order(10)
    @DisplayName("Consulta saldo afiliado")
    void consultaSaldoAfiliadoComSucessoRetornaMoedas() throws JsonProcessingException {

        var content =
                given()
                        .spec(specification("/saldo/{idCliente}"))
                        .contentType(CONTENT_TYPE_JSON)
                        .pathParam("uidApp", app().getUid())
                        .pathParam("idCliente", clienteAfiliado().getClienteId().getId())
                        .when()
                        .get()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body().asString();

        var padrinhoSaldoDTO = objectMapper.readValue(content, PadrinhoSaldoDTO.class);

        assertNotNull(padrinhoSaldoDTO);

        assertTrue(padrinhoSaldoDTO.getSaldoAtual().intValue() >= produto().getMoedaAfiliado());
    }

    @Test
    @Order(11)
    @DisplayName("Consulta meus afiliados")
    void consultaMeusAfiliadosComSucessoRetornaAfiliados() throws JsonProcessingException {

        var content =
                given()
                        .spec(specification("/{idCliente}/meusAfiliados"))
                        .contentType(CONTENT_TYPE_JSON)
                        .pathParam("uidApp", app().getUid())
                        .pathParam("idCliente", clientePadrinho().getClienteId().getId())
                        .queryParams("dataInicio", LocalDate.now().toString(),
                                "dataFim", LocalDate.now().plusDays(1).toString())
                        .when()
                        .get()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body().asString();

        var afiliadosDTO = objectMapper.readValue(content, new TypeReference<List<AfiliadoDTO>>() {
        });

        assertNotNull(afiliadosDTO);

        if (!afiliadosDTO.isEmpty()) {
            var afiliadoDTO = afiliadosDTO.get(0);

            assertEquals(afiliadoDTO.getPromocode(), PROMO_CODE);
            assertEquals(afiliadoDTO.getMoedas(), produto().getMoedaAfiliado());
            assertEquals(afiliadoDTO.getNomeAfiliado(), clienteAfiliado().getNome());
        }
    }

    @Test
    @Order(12)
    @DisplayName("Consulta extrato padrinho")
    void consultaExtratoComSucessoRetornaAfiliados() throws JsonProcessingException {

        var content =
                given()
                        .spec(specification("/{idCliente}/extrato"))
                        .contentType(CONTENT_TYPE_JSON)
                        .pathParam("uidApp", app().getUid())
                        .pathParam("idCliente", clientePadrinho().getClienteId().getId())
                        .queryParams("dataInicio", LocalDate.now().toString(),
                                "dataFim", LocalDate.now().plusDays(1).toString())
                        .when()
                        .get()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body().asString();

        var extratosDTO = objectMapper.readValue(content, new TypeReference<List<ExtratoDTO>>() {
        });

        assertNotNull(extratosDTO);

        if (extratosDTO.size() > 1) {

            var eventoPadrinho = extratosDTO.get(0);

            assertEquals(eventoPadrinho.getPromocode(), PROMO_CODE);
            assertEquals(eventoPadrinho.getMoedas(), produto().getMoedaPadrinho());
            assertEquals("PADRINHO", eventoPadrinho.getTipo());

        } else {

            var eventoManual = extratosDTO.get(0);

            assertEquals("", eventoManual.getPromocode());
            assertEquals(10, eventoManual.getMoedas());
            assertEquals("MANUAL", eventoManual.getTipo());
        }
    }

}

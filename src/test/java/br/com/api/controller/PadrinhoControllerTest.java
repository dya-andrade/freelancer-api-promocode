package br.com.api.controller;

import br.com.api.AbstractIntegrationTest;
import br.com.api.dto.AfiliadoSaldoDTO;
import br.com.api.dto.PadrinhoSaldoDTO;
import br.com.api.dto.PromoCodeDTO;
import br.com.api.dto.RetornoDTO;
import br.com.api.exception.model.ExceptionResponse;
import br.com.api.util.MockObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static br.com.api.exception.util.MessageException.EVENTO_SALDO_INSUFICIENTE;
import static br.com.api.util.MockObject.PROMO_CODE;
import static br.com.api.util.MockObject.eventoManualADD;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PadrinhoControllerTest extends AbstractIntegrationTest {

    private static ObjectMapper objectMapper;

    private static PromoCodeDTO promoCodeDTO;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    private static RequestSpecification specification(String url) {
        return new RequestSpecBuilder()
            .addHeader(MockObject.HEADER_PARAM_AUTHORIZATION, MockObject.TOKEN)
            .setBasePath("/{uidApp}/padrinho" + url)
            .setPort(MockObject.SERVER_PORT)
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
                .contentType(MockObject.CONTENT_TYPE_JSON)
                .pathParam("uidApp", MockObject.app().getUid())
                .pathParam("idCliente", MockObject.clientePadrinho().getClienteId().getId())
                .body(MockObject.eventoManualDTOADD())
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
    void criaEventoManualDuplicadoComRetornaFalse() throws JsonProcessingException {

        var content =
            given()
                .spec(specification("/{idCliente}/evento-manual"))
                .contentType(MockObject.CONTENT_TYPE_JSON)
                .pathParam("uidApp", MockObject.app().getUid())
                .pathParam("idCliente", MockObject.clientePadrinho().getClienteId().getId())
                .body(MockObject.eventoManualDTOADD())
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
                .contentType(MockObject.CONTENT_TYPE_JSON)
                .pathParam("uidApp", MockObject.app().getUid())
                .pathParam("idCliente", MockObject.clientePadrinho().getClienteId().getId())
                .queryParams("dataInicio", "2022-11-20", "dataFim", "2022-11-23")
                .body(MockObject.clientePadrinhoDTO())
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
                .contentType(MockObject.CONTENT_TYPE_JSON)
                .pathParam("uidApp", MockObject.app().getUid())
                .pathParam("idCliente", MockObject.clientePadrinho().getClienteId().getId())
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body().asString();

        var padrinhoSaldoDTO = objectMapper.readValue(content, PadrinhoSaldoDTO.class);

        assertNotNull(padrinhoSaldoDTO);

        assertEquals(padrinhoSaldoDTO.getSaldoAtual().intValue(), promoCodeDTO.getMoedas() + eventoManualADD().getMoeda());
    }

    @Test
    @Order(5)
    @DisplayName("Consulta detalhada afiliado")
    void consultaDetalhadaAfiliadoComSucessoRetornaAplicacoesVazias() throws JsonProcessingException {

        var content =
            given()
                .spec(specification("/detalhes/{idCliente}"))
                .contentType(MockObject.CONTENT_TYPE_JSON)
                .pathParam("uidApp", MockObject.app().getUid())
                .pathParam("idCliente", MockObject.clienteAfiliado2().getClienteId().getId())
                .queryParams("dataInicio", "2022-11-20", "dataFim", "2022-11-23")
                .body(MockObject.clienteAfiliadoDTO2())
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
                .contentType(MockObject.CONTENT_TYPE_JSON)
                .pathParam("uidApp", MockObject.app().getUid())
                .pathParam("idCliente", MockObject.clientePadrinho().getClienteId().getId())
                .pathParam("produtoId", MockObject.produto().getProdutoId().getId())
                .when()
                .put()
                .then()
                .statusCode(201)
                .extract()
                .body().asString();

        var promoCodeDTO = objectMapper.readValue(content, PromoCodeDTO.class);

        assertNotNull(promoCodeDTO);
        assertEquals(promoCodeDTO.getCode(), PROMO_CODE);
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
                .contentType(MockObject.CONTENT_TYPE_JSON)
                .pathParam("uidApp", MockObject.app().getUid())
                .pathParam("idCliente", MockObject.clienteAfiliado2().getClienteId().getId())
                .pathParam("produtoId", MockObject.produto().getProdutoId().getId())
                .when()
                .put()
                .then()
                .statusCode(201)
                .extract()
                .body().asString();

        promoCodeDTO = objectMapper.readValue(content, PromoCodeDTO.class);

        assertNotNull(promoCodeDTO);
        assertTrue(!promoCodeDTO.getCode().isBlank());
        assertTrue(promoCodeDTO.getQtdAfiliados() >= 0);
        assertTrue(promoCodeDTO.getQtdAtivacoes() >= 0);
        assertTrue(promoCodeDTO.getMoedas() >= 0);
    }

    @Test
    @Order(8)
    @DisplayName("Retorna moedas aplicação")
    void aplicaPromoCodeAfiliadoComSucessoRetornaMoedas() throws JsonProcessingException {

        var specification = new RequestSpecBuilder()
            .addHeader(MockObject.HEADER_PARAM_AUTHORIZATION, MockObject.TOKEN)
            .setBasePath("/{uidApp}/afiliado/{idCliente}/aplicar/{promocode}")
            .setPort(MockObject.SERVER_PORT)
            .addFilter(new RequestLoggingFilter(LogDetail.ALL))
            .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();

        var content =
            given()
                .spec(specification)
                .contentType(MockObject.CONTENT_TYPE_JSON)
                .pathParam("uidApp", MockObject.app().getUid())
                .pathParam("idCliente", MockObject.clienteAfiliado().getClienteId().getId())
                .pathParam("promocode", promoCodeDTO.getCode())
                .body(MockObject.clienteAfiliadoDTO())
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body().asString();

        var afiliadoSaldoDTO = objectMapper.readValue(content, AfiliadoSaldoDTO.class);

        assertNotNull(afiliadoSaldoDTO);

        assertEquals(afiliadoSaldoDTO.getMoedas(), MockObject.produto().getMoedaAfiliado());
    }

    @Test
    @Order(9)
    @DisplayName("Cria evento manual de RET com saldo insuficiente")
    void criaEventoManualSaldoInsuficienteTComRetornaErro() throws JsonProcessingException {

        var content =
            given()
                .spec(specification("/{idCliente}/evento-manual"))
                .contentType(MockObject.CONTENT_TYPE_JSON)
                .pathParam("uidApp", MockObject.app().getUid())
                .pathParam("idCliente", MockObject.clienteAfiliado().getClienteId().getId())
                .body(MockObject.eventoManualDTORET())
                .when()
                .post()
                .then()
                .statusCode(409)
                .extract()
                .body().asString();

        var exceptionResponse = objectMapper.readValue(content, ExceptionResponse.class);

        assertNotNull(exceptionResponse);

        assertEquals(exceptionResponse.getMessage(), EVENTO_SALDO_INSUFICIENTE);
    }

    @Test
    @Order(10)
    @DisplayName("Consulta saldo afiliado")
    void consultaSaldoAfiliadoComSucessoRetornaMoedas() throws JsonProcessingException {

        var content =
            given()
                .spec(specification("/saldo/{idCliente}"))
                .contentType(MockObject.CONTENT_TYPE_JSON)
                .pathParam("uidApp", MockObject.app().getUid())
                .pathParam("idCliente", MockObject.clienteAfiliado().getClienteId().getId())
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body().asString();

        var padrinhoSaldoDTO = objectMapper.readValue(content, PadrinhoSaldoDTO.class);

        assertNotNull(padrinhoSaldoDTO);

        assertEquals(padrinhoSaldoDTO.getSaldoAtual().intValue(), promoCodeDTO.getMoedas());
    }

}

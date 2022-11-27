package br.com.api.controller;

import br.com.api.AbstractIntegration;
import br.com.api.dto.AfiliadoSaldoDTO;
import br.com.api.dto.RetornoDTO;
import br.com.api.exception.model.ExceptionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static br.com.api.exception.util.MessageError.*;
import static br.com.api.util.MockObject.*;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AfiliadoControllerTest extends AbstractIntegration {

    private static ObjectMapper objectMapper;


    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    private static RequestSpecification specification(String url) {
        return new RequestSpecBuilder()
                .addHeader(HEADER_PARAM_AUTHORIZATION, TOKEN)
                .setBasePath("/{uidApp}/afiliado/{idCliente}" + url)
                .setPort(SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(1)
    @DisplayName("Retorna moedas aplicação")
    void aplicaPromoCodeAfiliadoComSucessoRetornaMoedas() throws JsonProcessingException {

        var content =
                given()
                        .spec(specification("/aplicar/{promocode}"))
                        .contentType(CONTENT_TYPE_JSON)
                        .pathParam("uidApp", app().getUid())
                        .pathParam("idCliente", clienteAfiliado().getClienteId().getId())
                        .pathParam("promocode", promoCode().getPromoCode())
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
    @Order(2)
    @DisplayName("Promocode já aplicado")
    void aplicaPromoCodeAfiliadoComPromoCodeJaAplicado() throws JsonProcessingException {

        var content =
                given()
                        .spec(specification("/aplicar/{promocode}"))
                        .contentType(CONTENT_TYPE_JSON)
                        .pathParam("uidApp", app().getUid())
                        .pathParam("idCliente", clienteAfiliado().getClienteId().getId())
                        .pathParam("promocode", promoCode().getPromoCode())
                        .body(clienteAfiliadoDTO())
                        .when()
                        .post()
                        .then()
                        .statusCode(409)
                        .extract()
                        .body().asString();

        var exceptionResponse = objectMapper.readValue(content, ExceptionResponse.class);

        assertNotNull(exceptionResponse);

        assertEquals(PROMOCODE_JA_APLICADO, exceptionResponse.getMessage());
    }

    @Test
    @Order(3)
    @DisplayName("Afiliado é padrinho do promocode")
    void aplicaPromoCodeAfiliadoComPadrinhoComoAfiliado() throws JsonProcessingException {

        var content =
                given()
                        .spec(specification("/aplicar/{promocode}"))
                        .contentType(CONTENT_TYPE_JSON)
                        .pathParam("uidApp", app().getUid())
                        .pathParam("idCliente", clientePadrinho().getClienteId().getId())
                        .pathParam("promocode", promoCode().getPromoCode())
                        .body(clientePadrinhoDTO())
                        .when()
                        .post()
                        .then()
                        .statusCode(409)
                        .extract()
                        .body().asString();

        var exceptionResponse = objectMapper.readValue(content, ExceptionResponse.class);

        assertNotNull(exceptionResponse);

        assertEquals(PROMOCODE_APLICACAO_PADRINHO, exceptionResponse.getMessage());
    }

    @Test
    @Order(4)
    @DisplayName("Promocode está expirado")
    void aplicaPromoCodeAfiliadoComPromoCodeExpirado() throws JsonProcessingException {

        var content =
                given()
                        .spec(specification("/aplicar/{promocode}"))
                        .contentType(CONTENT_TYPE_JSON)
                        .pathParam("uidApp", app().getUid())
                        .pathParam("idCliente", clienteAfiliado2().getClienteId().getId())
                        .pathParam("promocode", promoCode().getPromoCode())
                        .body(clienteAfiliadoDTO2())
                        .when()
                        .post()
                        .then()
                        .statusCode(409)
                        .extract()
                        .body().asString();

        var exceptionResponse = objectMapper.readValue(content, ExceptionResponse.class);

        assertNotNull(exceptionResponse);

        assertEquals(PROMOCODE_EXPIRADO, exceptionResponse.getMessage());
    }

    @Test
    @Order(5)
    @DisplayName("Aplica evento padrinho")
    void aplicaPadrinhoComSucessoRetornaOk() throws JsonProcessingException {

        var content =
                given()
                        .spec(specification("/aplicarPadrinho/{idReferencia}"))
                        .contentType(CONTENT_TYPE_JSON)
                        .pathParam("uidApp", app().getUid())
                        .pathParam("idCliente", clienteAfiliado().getClienteId().getId())
                        .pathParam("idReferencia", eventoPadrinho().getIdReferencia())
                        .when()
                        .post()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body().asString();

        var retornoDTO = objectMapper.readValue(content, RetornoDTO.class);

        assertNotNull(retornoDTO);

        assertTrue(retornoDTO.getOk());
    }

    @Test
    @Order(6)
    @DisplayName("Aplica evento padrinho duplicado")
    void aplicaPadrinhoDuplicadoERetornaFalse() throws JsonProcessingException {

        var content =
                given()
                        .spec(specification("/aplicarPadrinho/{idReferencia}"))
                        .contentType(CONTENT_TYPE_JSON)
                        .pathParam("uidApp", app().getUid())
                        .pathParam("idCliente", clienteAfiliado().getClienteId().getId())
                        .pathParam("idReferencia", eventoPadrinho().getIdReferencia())
                        .when()
                        .post()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body().asString();

        var retornoDTO = objectMapper.readValue(content, RetornoDTO.class);

        assertNotNull(retornoDTO);

        assertFalse(retornoDTO.getOk());
    }

}

package br.com.api.controller;

import br.com.api.AbstractIntegrationTest;
import br.com.api.dto.AfiliadoSaldoDTO;
import br.com.api.exception.model.ExceptionResponse;
import br.com.api.util.MockObject;
import com.fasterxml.jackson.core.JsonProcessingException;
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

import static br.com.api.exception.util.MessageException.PROMOCODE_APLICACAO_PADRINHO;
import static br.com.api.exception.util.MessageException.PROMOCODE_EXPIRADO;
import static br.com.api.exception.util.MessageException.PROMOCODE_JA_APLICADO;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AfiliadoControllerTest extends AbstractIntegrationTest {

    private static ObjectMapper objectMapper;


    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    private static RequestSpecification specification() {
        return new RequestSpecBuilder()
            .addHeader(MockObject.HEADER_PARAM_AUTHORIZATION, MockObject.TOKEN)
            .setBasePath("/{uidApp}/afiliado/{idCliente}/aplicar/{promocode}")
            .setPort(MockObject.SERVER_PORT)
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
                .spec(specification())
                .contentType(MockObject.CONTENT_TYPE_JSON)
                .pathParam("uidApp", MockObject.app().getUid())
                .pathParam("idCliente", MockObject.clienteAfiliado().getClienteId().getId())
                .pathParam("promocode", MockObject.promoCode().getPromoCode())
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
    @Order(2)
    @DisplayName("Promocode já aplicado")
    void aplicaPromoCodeAfiliadoComPromoCodeJaAplicado() throws JsonProcessingException {

        var content =
            given()
                .spec(specification())
                .contentType(MockObject.CONTENT_TYPE_JSON)
                .pathParam("uidApp", MockObject.app().getUid())
                .pathParam("idCliente", MockObject.clienteAfiliado().getClienteId().getId())
                .pathParam("promocode", MockObject.promoCode().getPromoCode())
                .body(MockObject.clienteAfiliadoDTO())
                .when()
                .post()
                .then()
                .statusCode(409)
                .extract()
                .body().asString();

        var exceptionResponse = objectMapper.readValue(content, ExceptionResponse.class);

        assertNotNull(exceptionResponse);

        assertEquals(exceptionResponse.getMessage(), PROMOCODE_JA_APLICADO);
    }

    @Test
    @Order(3)
    @DisplayName("Afiliado é padrinho do promocode")
    void aplicaPromoCodeAfiliadoComPadrinhoComoAfiliado() throws JsonProcessingException {

        var content =
            given()
                .spec(specification())
                .contentType(MockObject.CONTENT_TYPE_JSON)
                .pathParam("uidApp", MockObject.app().getUid())
                .pathParam("idCliente", MockObject.clientePadrinho().getClienteId().getId())
                .pathParam("promocode", MockObject.promoCode().getPromoCode())
                .body(MockObject.clientePadrinhoDTO())
                .when()
                .post()
                .then()
                .statusCode(409)
                .extract()
                .body().asString();

        var exceptionResponse = objectMapper.readValue(content, ExceptionResponse.class);

        assertNotNull(exceptionResponse);

        assertEquals(exceptionResponse.getMessage(), PROMOCODE_APLICACAO_PADRINHO);
    }

    @Test
    @Order(4)
    @DisplayName("Promocode está expirado")
    void aplicaPromoCodeAfiliadoComPromoCodeExpirado() throws JsonProcessingException {

        var content =
            given()
                .spec(specification())
                .contentType(MockObject.CONTENT_TYPE_JSON)
                .pathParam("uidApp", MockObject.app().getUid())
                .pathParam("idCliente", MockObject.clienteAfiliado2().getClienteId().getId())
                .pathParam("promocode", MockObject.promoCode().getPromoCode())
                .body(MockObject.clienteAfiliadoDTO2())
                .when()
                .post()
                .then()
                .statusCode(409)
                .extract()
                .body().asString();

        var exceptionResponse = objectMapper.readValue(content, ExceptionResponse.class);

        assertNotNull(exceptionResponse);

        assertEquals(exceptionResponse.getMessage(), PROMOCODE_EXPIRADO);
    }

}

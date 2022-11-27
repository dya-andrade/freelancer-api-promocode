package br.com.api.controller.api;

import br.com.api.dto.AfiliadoSaldoDTO;
import br.com.api.dto.ClienteDTO;
import br.com.api.dto.RetornoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Afiliado", description = "Endpoints do afiliado.")
@ApiResponse(responseCode = "500", description = "Internal Error.")
public interface AfiliadoApi {

    @Operation(summary = "Aplica PromoCode", description = "Endpoint para aplicar promocode, com sucesso, retorna o saldo da aplicação.",
            tags = {"Afiliado"}, responses = {@ApiResponse(description = "Success", responseCode = "200",
            content = @Content(schema = @Schema(implementation = AfiliadoSaldoDTO.class))),

            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),})
    AfiliadoSaldoDTO aplicaPromoCode(String token, String uidApp, String idCliente, String promocode, ClienteDTO clienteAfiliadoDTO);

    @Operation(summary = "Aplica Padrinho", description = "Endpoint para aplicar padrinho, com sucesso, retorna o saldo da aplicação.",
            tags = {"Afiliado"}, responses = {@ApiResponse(description = "Success", responseCode = "200",
            content = @Content(schema = @Schema(implementation = RetornoDTO.class))),

            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),})
    RetornoDTO aplicaPadrinho(String token, String uidApp, String idCliente, String idReferencia);

}

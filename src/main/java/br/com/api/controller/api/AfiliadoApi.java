package br.com.api.controller.api;

import br.com.api.dto.AfiliadoSaldoDTO;
import br.com.api.dto.ClienteDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Validated
@Tag(name = "Afiliado", description = "Endpoints do afiliado.")
@ApiResponse(responseCode = "500", description = "Internal Error.")
public interface AfiliadoApi {

    @Operation(summary = "Aplica PromoCode", description = "Endpoint para aplicar promocode, com sucesso, retorna o saldo do afiliado.",
        tags = { "Afiliado" }, responses = { @ApiResponse(description = "Success", responseCode = "200",
        content = @Content(schema = @Schema(implementation = AfiliadoSaldoDTO.class))),

        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
        @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
        @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content), })
    AfiliadoSaldoDTO aplicaPromoCode(@NotBlank String token, @NotBlank String uidApp, @NotBlank String idCliente,
        @NotBlank String promocode, @NotNull ClienteDTO clienteAfiliadoDTO);

}

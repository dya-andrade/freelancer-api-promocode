package br.com.api.controller;

import br.com.api.controller.api.AfiliadoApi;
import br.com.api.dto.AfiliadoSaldoDTO;
import br.com.api.dto.ClienteDTO;
import br.com.api.dto.RetornoDTO;
import br.com.api.service.AfiliadoService;
import br.com.api.service.AppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/{uidApp}/afiliado")
public class AfiliadoController implements AfiliadoApi {

    private final AppService appService;

    private final AfiliadoService afiliadoService;

    @Override
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{idCliente}/aplicar/{promocode}")
    public AfiliadoSaldoDTO aplicaPromoCode(
        @RequestHeader(name = "token") String token, @PathVariable(value = "uidApp") String uidApp,
        @PathVariable(value = "idCliente") String idCliente, @PathVariable(value = "promocode") String promocode,
        @RequestBody @Valid ClienteDTO clienteAfiliadoDTO) {

        var app = appService.autenticaApp(uidApp, token);

        return afiliadoService.aplicaPromoCode(app, idCliente, promocode, clienteAfiliadoDTO);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{idCliente}/aplicarPadrinho/{idReferencia}")
    public RetornoDTO aplicaPadrinho(
            @RequestHeader(name = "token") String token, @PathVariable(value = "uidApp") String uidApp,
            @PathVariable(value = "idCliente") String idCliente, @PathVariable(value = "idReferencia") String idReferencia) {

        var app = appService.autenticaApp(uidApp, token);

        return afiliadoService.aplicaPadrinho(app, idCliente, idReferencia);
    }
}

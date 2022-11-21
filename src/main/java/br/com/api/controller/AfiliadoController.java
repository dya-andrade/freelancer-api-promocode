package br.com.api.controller;

import br.com.api.controller.api.AfiliadoApi;
import br.com.api.dto.AfiliadoSaldoDTO;
import br.com.api.dto.ClienteDTO;
import br.com.api.service.AfiliadoService;
import br.com.api.service.AppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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

}

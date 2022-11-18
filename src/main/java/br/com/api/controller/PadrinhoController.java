package br.com.api.controller;

import br.com.api.dto.ClientePadrinhoDTO;
import br.com.api.dto.PadrinhoSaldoDTO;
import br.com.api.dto.PromoCodeDTO;
import br.com.api.service.AppService;
import br.com.api.service.PadrinhoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/{uidApp}/padrinho")
public class PadrinhoController {

    private final PadrinhoService padrinhoService;

    private final AppService appService;


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/saldo/{idCliente}")
    public PadrinhoSaldoDTO consultaSaldo(
        @RequestHeader(name = "token") String token, @PathVariable(value = "uidApp") UUID uidApp,
        @PathVariable(value = "idCliente") String idCliente) {

        var app = appService.autenticaApp(uidApp, token);

        return padrinhoService.consultaSaldo(app, idCliente);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/detalhes/{idCliente}")
    public List<PromoCodeDTO> consultaDetalhes(
        @RequestHeader(name = "token") String token, @PathVariable(value = "uidApp") UUID uidApp,
        @PathVariable(value = "idCliente") String idCliente, @RequestParam(value = "dataInicio") Date dataInicio,
        @RequestParam(value = "dataFim") Date dataFim, @RequestBody @Valid ClientePadrinhoDTO clientePadrinhoDTO) {

        var app = appService.autenticaApp(uidApp, token);

        return padrinhoService.consultaDetalhes(app, idCliente, dataInicio, dataFim, clientePadrinhoDTO);
    }

}

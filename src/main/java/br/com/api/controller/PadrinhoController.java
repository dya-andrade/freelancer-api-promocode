package br.com.api.controller;

import br.com.api.controller.api.PadrinhoApi;
import br.com.api.dto.ClienteDTO;
import br.com.api.dto.EventoManualDTO;
import br.com.api.dto.PadrinhoSaldoDTO;
import br.com.api.dto.PromoCodeDTO;
import br.com.api.dto.RetornoDTO;
import br.com.api.service.AppService;
import br.com.api.service.EventoManualService;
import br.com.api.service.PadrinhoService;
import br.com.api.service.PromoCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/{uidApp}/padrinho")
public class PadrinhoController implements PadrinhoApi {

    private final PadrinhoService padrinhoService;

    private final PromoCodeService promoCodeService;

    private final EventoManualService eventoManualService;

    private final AppService appService;

    @Override
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/saldo/{idCliente}")
    public PadrinhoSaldoDTO consultaSaldo(
        @RequestHeader(name = "token") String token, @PathVariable(value = "uidApp") String uidApp,
        @PathVariable(value = "idCliente") String idCliente) {

        var app = appService.autenticaApp(uidApp, token);

        return padrinhoService.consultaSaldo(app, idCliente);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/detalhes/{idCliente}")
    public List<PromoCodeDTO> consultaDetalhada(
        @RequestHeader(name = "token") String token, @PathVariable(value = "uidApp") String uidApp,
        @PathVariable(value = "idCliente") String idCliente, @RequestParam(value = "dataInicio") String dataInicio,
        @RequestParam(value = "dataFim") String dataFim, @RequestBody @Valid ClienteDTO clientePadrinhoDTO) {

        var app = appService.autenticaApp(uidApp, token);

        return padrinhoService.consultaDetalhada(app, idCliente, dataInicio, dataFim, clientePadrinhoDTO);
    }

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{idCliente}/promocode/{produtoId}")
    public PromoCodeDTO criaPromoCode(
        @RequestHeader(name = "token") String token, @PathVariable(value = "uidApp") String uidApp,
        @PathVariable(value = "idCliente") String idCliente, @PathVariable(value = "produtoId") String produtoId) {

        var app = appService.autenticaApp(uidApp, token);

        return promoCodeService.criaPromoCode(app, idCliente, produtoId);
    }

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{idCliente}/evento-manual")
    public RetornoDTO criaEventoManual(
        @RequestHeader(name = "token") String token, @PathVariable(value = "uidApp") String uidApp,
        @PathVariable(value = "idCliente") String idCliente, @RequestBody @Valid EventoManualDTO eventoManualDTO) {

        var app = appService.autenticaApp(uidApp, token);

        return eventoManualService.criaEventoManual(app, idCliente, eventoManualDTO);
    }

}

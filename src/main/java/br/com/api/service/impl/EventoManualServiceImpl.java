package br.com.api.service.impl;

import br.com.api.dto.EventoManualDTO;
import br.com.api.dto.RetornoDTO;
import br.com.api.exception.ResourceConflictException;
import br.com.api.model.App;
import br.com.api.model.Cliente;
import br.com.api.model.EventoManual;
import br.com.api.model.EventoManualID;
import br.com.api.repository.EventoManualRepository;
import br.com.api.service.EventoManualService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

import static br.com.api.model.TipoEventoManual.RET;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class EventoManualServiceImpl implements EventoManualService {

    private final EventoManualRepository eventoManualRepository;
    private final ClienteServiceImpl clienteService;
    private final PadrinhoServiceImpl padrinhoService;

    private static RetornoDTO buildRetornoDTO(Boolean ok) {
        return RetornoDTO.
            builder()
            .ok(ok)
            .build();
    }

    private RetornoDTO validaEventoManualDuplicidade(final Cliente cliente, final EventoManualDTO eventoManualDTO) {

        EventoManual eventoManual = null;

        if (nonNull(eventoManualDTO.getIdReferencia()) && !eventoManualDTO.getIdReferencia().isBlank()) {
            eventoManual = eventoManualRepository
                .findByEventoManualIdClienteAndIdReferencia(cliente, eventoManualDTO.getIdReferencia());
        }

        if (isNull(eventoManual)) {
            eventoManualRepository.save(
                EventoManual.builder()
                    .eventoManualId(EventoManualID.builder()
                        .uid(geraUIDEventoManual())
                        .cliente(cliente)
                        .build())
                    .moeda(eventoManualDTO.getMoeda())
                    .motivo(eventoManualDTO.getMotivo())
                    .tipo(eventoManualDTO.getTipo())
                    .idReferencia(eventoManualDTO.getIdReferencia())
                    .dtCriacao(LocalDateTime.now())
                    .build());

            return buildRetornoDTO(true);
        }

        return buildRetornoDTO(false);
    }

    private String geraUIDEventoManual() {
        var uid = UUID.randomUUID().toString();

        var eventoManualOptional = eventoManualRepository
            .findByEventoManualIdUid(uid);

        if (eventoManualOptional.isPresent())
            return geraUIDEventoManual();

        return uid;
    }

    @Override
    public RetornoDTO criaEventoManual(final App app, final String idCliente, final EventoManualDTO eventoManualDTO) {
        var cliente = clienteService.buscaCliente(app, idCliente);

        if (eventoManualDTO.getTipo().equals(RET)) {
            var saldoPadrinho = padrinhoService.consultaSaldo(app, idCliente).getSaldoAtual();

            if (saldoPadrinho >= eventoManualDTO.getMoeda()) {
                return validaEventoManualDuplicidade(cliente, eventoManualDTO);
            }

            throw new ResourceConflictException("Erro ao tentar criar evento, saldo insuficiente.");
        }

        return validaEventoManualDuplicidade(cliente, eventoManualDTO);
    }

}

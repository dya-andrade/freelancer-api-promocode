package br.com.api.service.impl;

import br.com.api.dto.ClienteDTO;
import br.com.api.exception.ResourceNotFoundException;
import br.com.api.model.App;
import br.com.api.model.Cliente;
import br.com.api.model.ClienteID;
import br.com.api.repository.ClienteRepository;
import br.com.api.service.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    private static ClienteID buildClienteID(final App app, final String idCliente) {
        return ClienteID.builder()
            .id(idCliente)
            .app(app)
            .build();
    }

    @Override
    public Cliente validaClienteExistente(final App app, final String idCliente, final ClienteDTO clienteDTO) {
        var cliente = clienteRepository.findById(buildClienteID(app, idCliente));

        if (cliente.isEmpty())
            return clienteRepository.save(
                Cliente.builder()
                    .clienteId(buildClienteID(app, idCliente))
                    .nome(clienteDTO.getNome())
                    .email(clienteDTO.getEmail())
                    .dtCriacao(LocalDateTime.now())
                    .build());

        return cliente.get();
    }

    @Override
    public Cliente buscaCliente(final App app, final String idCliente){
        return clienteRepository.findById(buildClienteID(app, idCliente))
            .orElseThrow(() -> new ResourceNotFoundException("Erro ao tentar buscar cliente, ID não encontrado."));
    }
}

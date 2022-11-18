package br.com.api.service;

import br.com.api.exception.InvalidAuthenticationException;
import br.com.api.model.App;
import br.com.api.repository.AppRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Service
@Validated
@RequiredArgsConstructor
public class AppService {

    private final AppRepository appRepository;

    public App autenticaApp(@NotNull UUID uidApp, @NotBlank String token) {
        return appRepository.findByUidAndToken(uidApp, token)
            .orElseThrow(() -> new InvalidAuthenticationException("Erro ao tentar autenticar App, UID ou Token inválidos."));
    }

}

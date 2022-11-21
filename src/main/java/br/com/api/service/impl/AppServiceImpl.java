package br.com.api.service.impl;

import br.com.api.exception.InvalidAuthenticationException;
import br.com.api.model.App;
import br.com.api.repository.AppRepository;
import br.com.api.service.AppService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class AppServiceImpl implements AppService {

    private final AppRepository appRepository;

    @Override
    public App autenticaApp(final String uidApp, final String token) {
        return appRepository.findByUidAndToken(uidApp, token)
            .orElseThrow(() -> new InvalidAuthenticationException("Erro ao tentar autenticar App, UID ou Token inválidos."));
    }

}

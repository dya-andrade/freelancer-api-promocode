package br.com.api.service;

import br.com.api.model.App;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Validated
public interface AppService {

    App autenticaApp(@NotBlank String uidApp, @NotBlank String token);

}

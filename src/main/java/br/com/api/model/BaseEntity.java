package br.com.api.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public abstract class BaseEntity {

    private LocalDateTime dtCriacao;
    private LocalDateTime dtAlteracao;
    private LocalDateTime dtExclusao;
}

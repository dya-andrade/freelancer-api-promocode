package br.com.api.model;

import lombok.Data;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Data
@MappedSuperclass
public abstract class BaseEntity {

    private LocalDateTime dtCriacao;
    private LocalDateTime dtAlteracao;
    private LocalDateTime dtExclusao;
}

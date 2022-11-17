package br.com.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@Entity
@SuperBuilder
@Table(name = "cliente")
@EqualsAndHashCode(callSuper = true)
public class Cliente extends BaseEntity {

    @EmbeddedId
    private ClienteId clienteId;
    private String nome;
    private String email;
}

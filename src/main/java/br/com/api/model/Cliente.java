package br.com.api.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity
@Builder
@Table(name = "cliente")
@EqualsAndHashCode(callSuper = true)
public class Cliente extends BaseEntity {

    @EmbeddedId
    private ClienteId clienteId;
    private String nome;
    private String email;
}

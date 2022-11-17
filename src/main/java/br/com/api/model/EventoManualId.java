package br.com.api.model;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.UUID;

@Data
@Embeddable
public class EventoManualId {

    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uid;
    private Cliente clienteId;
}

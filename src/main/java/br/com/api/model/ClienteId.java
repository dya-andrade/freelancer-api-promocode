package br.com.api.model;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
@Embeddable
public class ClienteId implements Serializable {

    private String id;
    @ManyToOne
    private App app;
}

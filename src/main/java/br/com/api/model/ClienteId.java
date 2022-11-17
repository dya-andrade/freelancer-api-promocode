package br.com.api.model;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Data
@Embeddable
public class ClienteId {

    private String id;
    private App appId;
}

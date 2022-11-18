package br.com.api.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
@Builder
@Embeddable
public class ClienteID implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    @ManyToOne
    private App app;
}

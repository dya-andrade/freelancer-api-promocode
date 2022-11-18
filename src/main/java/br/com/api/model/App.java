package br.com.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;


@Data
@Entity
@SuperBuilder
@Table(name = "app")
@EqualsAndHashCode(callSuper = true)
public class App extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uid;
    private String nome;
    private String token;
}

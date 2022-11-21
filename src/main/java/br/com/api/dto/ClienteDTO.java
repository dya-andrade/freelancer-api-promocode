package br.com.api.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ClienteDTO {
    
    @NotBlank
    private String nome;
    @NotBlank
    private String email;
}

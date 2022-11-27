package br.com.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AfiliadoDTO {

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm", locale = "pt-BR", timezone = "Brazil/East")
    private LocalDateTime dtInclusao;
    private Integer moedas;
    private String nomeAfiliado;
    private String promocode;

}

package br.com.api.repository;

import br.com.api.dto.AfiliadoDTO;
import br.com.api.dto.ExtratoDTO;
import br.com.api.model.Cliente;
import br.com.api.model.EventoAfiliado;
import br.com.api.model.EventoAfiliadoID;
import br.com.api.model.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface EventoAfiliadoRepository extends JpaRepository<EventoAfiliado, EventoAfiliadoID> {

    @Query(value = "SELECT COUNT(ea)" +
            "  FROM EventoAfiliado ea INNER JOIN PromoCode pc " +
            "  ON ea.eventoAfiliadoId.promoCode = pc" +
            "  WHERE pc = :promoCode")
    Long consultaQtdAplicacoesPromoCode(PromoCode promoCode);

    @Query(value = "SELECT ea" +
            "  FROM EventoAfiliado ea INNER JOIN PromoCode pc " +
            "  ON ea.eventoAfiliadoId.promoCode = pc" +
            "  WHERE ea.eventoAfiliadoId.clienteAfiliado = :clienteAfiliado")
    List<EventoAfiliado> consultaEventosAfiliado(Cliente clienteAfiliado);

    @Query(value = "SELECT NEW " +
            "  br.com.api.dto.AfiliadoDTO(ea.dtCriacao, ea.moeda, ea.eventoAfiliadoId.clienteAfiliado.nome, pc.promoCode)" +
            "  FROM PromoCode pc INNER JOIN EventoAfiliado ea ON pc = ea.eventoAfiliadoId.promoCode" +
            "  WHERE pc.promoCodeId.clientePadrinho = :clientePadrinho AND ea.dtCriacao BETWEEN :dataInicio AND :dataFim" +
            "  GROUP BY pc.promoCode, ea.eventoAfiliadoId.clienteAfiliado.nome, ea.dtCriacao, ea.moeda")
    List<AfiliadoDTO> consultaMeusAfiliados(Cliente clientePadrinho, LocalDateTime dataInicio, LocalDateTime dataFim);

    @Query(value = "SELECT NEW br.com.api.dto.ExtratoDTO" +
            "  (ea.dtCriacao, ea.moeda, pc.promoCode, 'AFILIADO', 'Evento afiliado')" +
            "  FROM PromoCode pc INNER JOIN EventoAfiliado ea " +
            "  ON pc = ea.eventoAfiliadoId.promoCode" +
            "  WHERE ea.eventoAfiliadoId.clienteAfiliado = :clienteAfiliado " +
            "  AND ea.dtCriacao BETWEEN :dataInicio AND :dataFim " +
            "  GROUP BY pc.promoCode, ea.dtCriacao, ea.moeda")
    List<ExtratoDTO> consultaExtratoEventosAfiliado(Cliente clienteAfiliado, LocalDateTime dataInicio, LocalDateTime dataFim);

}

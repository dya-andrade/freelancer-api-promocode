package br.com.api.repository;

import br.com.api.dto.ExtratoDTO;
import br.com.api.model.Cliente;
import br.com.api.model.EventoManual;
import br.com.api.model.EventoManualID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface EventoManualRepository extends JpaRepository<EventoManual, EventoManualID> {

    EventoManual findByEventoManualIdClienteAndIdReferencia(Cliente cliente, String idReferencia);

    Optional<EventoManual> findByEventoManualIdUid(String uid);

    @Query(value = "SELECT NEW br.com.api.dto.ExtratoDTO" +
            "  (em.dtCriacao, em.moeda, '', 'MANUAL', em.motivo)" +
            "  FROM EventoManual em WHERE em.eventoManualId.cliente = :clientePadrinho " +
            "  AND em.dtCriacao BETWEEN :dataInicio AND :dataFim")
    List<ExtratoDTO> consultaExtratoEventosManuais(Cliente clientePadrinho, LocalDateTime dataInicio, LocalDateTime dataFim);

}

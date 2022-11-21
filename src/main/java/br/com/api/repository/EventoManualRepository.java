package br.com.api.repository;

import br.com.api.model.Cliente;
import br.com.api.model.EventoManual;
import br.com.api.model.EventoManualID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface EventoManualRepository extends JpaRepository<EventoManual, EventoManualID> {

    EventoManual findByEventoManualIdClienteAndIdReferencia(Cliente cliente, String idReferencia);

    Optional<EventoManual> findByEventoManualIdUid(String uid);

}

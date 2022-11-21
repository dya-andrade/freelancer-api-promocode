package br.com.api.repository;

import br.com.api.model.EventoAfiliado;
import br.com.api.model.EventoAfiliadoID;
import br.com.api.model.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface EventoAfiliadoRepository extends JpaRepository<EventoAfiliado, EventoAfiliadoID> {

    @Query(value = "SELECT COUNT(ea)" +
        "  FROM EventoAfiliado ea INNER JOIN PromoCode pc " +
        "  ON ea.eventoAfiliadoId.promoCode = pc" +
        "  WHERE ea.eventoAfiliadoId.promoCode = :promoCode")
    Long consultaQtdAplicacoesPromoCode(PromoCode promoCode);

}

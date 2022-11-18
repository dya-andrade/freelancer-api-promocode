package br.com.api.repository;

import br.com.api.dto.PadrinhoSaldoDTO;
import br.com.api.dto.PromoCodeDTO;
import br.com.api.model.Cliente;

import br.com.api.model.EventoPadrinho;
import br.com.api.model.EventoPadrinhoID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Repository
public interface EventoPadrinhoRepository extends JpaRepository<EventoPadrinho, EventoPadrinhoID> {

    @Query(value = "SELECT NEW " +
      "  br.com.api.dto.PadrinhoSaldoDTO(SUM(ep.moeda))" +
      "  FROM EventoAfiliado ea INNER JOIN EventoPadrinho ep" +
      "  WHERE ea.eventoAfiliadoId.clienteAfiliado = :clienteAfiliado")
    PadrinhoSaldoDTO findPadrinhoSaldo(Cliente clienteAfiliado);

    @Query(value = "SELECT NEW " +
        "  br.com.api.dto.PromoCodeDTO(pc.promoCode, COUNT(ep), COUNT(ea), SUM(ep.moeda))" +
        "  FROM PromoCode pc INNER JOIN EventoAfiliado ea INNER JOIN EventoPadrinho ep" +
        "  WHERE pc.promoCodeId.clientePadrinho = :clientePadrinho" +
        "  AND ea.dtCriacao BETWEEN :dataInicio AND :dataFim AND ep.dtCriacao BETWEEN :dataInicio AND :dataFim")
    List<PromoCodeDTO> findPromoCodes(Cliente clientePadrinho, Date dataInicio, Date dataFim);
}

package br.com.api.repository;

import br.com.api.model.Cliente;
import br.com.api.model.PromoCode;
import br.com.api.model.PromoCodeID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, PromoCodeID> {

    Optional<PromoCode> findByPromoCode(String promoCode);

    List<PromoCode> findByPromoCodeIdClientePadrinho(Cliente clientePadrinho);
}

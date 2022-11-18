package br.com.api.repository;

import br.com.api.model.Cliente;
import br.com.api.model.ClienteID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, ClienteID> {

}

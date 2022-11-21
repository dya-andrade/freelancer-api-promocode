package br.com.api.repository;

import br.com.api.model.Produto;
import br.com.api.model.ProdutoID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, ProdutoID> {

}

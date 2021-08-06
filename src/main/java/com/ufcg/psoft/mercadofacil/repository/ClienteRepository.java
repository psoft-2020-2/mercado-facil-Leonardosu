package com.ufcg.psoft.mercadofacil.repository;

import java.util.Optional;
import com.ufcg.psoft.mercadofacil.model.Cliente.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

	Optional<Cliente> findByCPF(long cpf);

	Cliente findClienteById(long id);
}

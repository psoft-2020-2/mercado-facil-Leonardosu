package com.ufcg.psoft.mercadofacil.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

import com.ufcg.psoft.mercadofacil.model.Notificador.Notificador;

public interface NotificadorRepository extends JpaRepository<Notificador, Long> {
    Notificador findNotificadorById(Long id);

    Set<Notificador> findAllNotificadorByTipo(String tipo);
}

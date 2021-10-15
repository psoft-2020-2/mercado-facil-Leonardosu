package com.ufcg.psoft.mercadofacil.service;

import java.util.Set;

import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.model.Cliente.Cliente;
import com.ufcg.psoft.mercadofacil.model.Notificador.*;
import com.ufcg.psoft.mercadofacil.repository.NotificadorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificadorServiceImpl implements NotificadorService {
    static final String ESTOQUE = "estoque";
    static final String PROMOCAO = "promocao";

    @Autowired
    NotificadorRepository notificadorRepository;

    @Override
    public void salvarClienteNotificador(String tipoNotificador, Produto produto, Cliente cliente) {

        Set<Notificador> notificadores = notificadorRepository.findAllNotificadorByTipo(tipoNotificador);

        if (notificadores.isEmpty()) {
            Notificador notificador = createNotificador(tipoNotificador, produto);
            notificadores.add(notificador);
        }

        Notificador notificador = searchByProduto(notificadores, produto.getId());
        if (notificador == null) {
            notificador = createNotificador(tipoNotificador, produto);
        }

        notificador.adicionaCliente(cliente);
        notificadorRepository.save(notificador);

    }

    @Override
    public Set<Cliente> listarClienteNotificadorByProduto(String tipoNotificador, Produto produto) {
        Set<Notificador> notificadores = notificadorRepository.findAllNotificadorByTipo(tipoNotificador);

        if (notificadores.isEmpty()) {
            Notificador notificador = createNotificador(tipoNotificador, produto);
            notificadores.add(notificador);
        }

        Notificador notificador = searchByProduto(notificadores, produto.getId());
        return notificador.getClientes();
    }

    @Override
    public String notificaByProduto(String tipoNotificador, Produto produto) {

        Set<Notificador> notificadores = notificadorRepository.findAllNotificadorByTipo(tipoNotificador);

        Notificador notificador = searchByProduto(notificadores, produto.getId());
        if (notificador == null)
            return "Notificador vazio";

        return notificador.notificaCliente();
    }

    private Notificador searchByProduto(Set<Notificador> notificadores, Long idProduto) {
        for (Notificador notificadorAtual : notificadores)
            if (notificadorAtual.getProduto().getId() == idProduto)
                return notificadorAtual;

        return null;
    }

    private Notificador createNotificador(String tipoNotificador, Produto produto) {
        Notificador notificador;

        if (tipoNotificador.equals(ESTOQUE))
            notificador = new NovoEstoque(produto);
        else
            notificador = new Promocao(produto);

        return notificador;
    }
}

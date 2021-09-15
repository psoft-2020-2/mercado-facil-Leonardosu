package com.ufcg.psoft.mercadofacil.service;

import java.util.Set;

import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.model.Cliente.Cliente;

public interface NotificadorService {

    public void salvarClienteNotificador(String tipoNotificador, Produto produto, Cliente cliente);

    public Set<Cliente> listarClienteNotificadorByProduto(String tipoNotificador, Produto produto);

    public String notificaByProduto(String tipoNotificador, Produto produto);
}

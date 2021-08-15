package com.ufcg.psoft.mercadofacil.service;

import java.util.List;
import java.util.Optional;

import com.ufcg.psoft.mercadofacil.model.Compra;

public interface CarrinhoService {

    public void salvarCompra(Compra compra);

    public void deletarCompra(Compra compra);

    public Optional<Compra> getByIdDoProduto(long idDoProduto);

    public List<Compra> listarCompras();

}
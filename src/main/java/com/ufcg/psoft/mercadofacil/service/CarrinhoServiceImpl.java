package com.ufcg.psoft.mercadofacil.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufcg.psoft.mercadofacil.model.Compra;
import com.ufcg.psoft.mercadofacil.repository.CarrinhoRepository;

@Service
public class CarrinhoServiceImpl implements CarrinhoService {

    @Autowired
    private CarrinhoRepository carrinho;

    public void salvarCompra(Compra compra) {
        carrinho.save(compra);
    }

    public List<Compra> listarCompras() {
        return carrinho.findAll();
    }

    public Optional<Compra> getByIdDoProduto(long idDoProduto) {
        return carrinho.findByIdDoProduto(idDoProduto);
    }

    @Override
    public void deletarCompra(Compra compra) {
        carrinho.delete(compra);
    }

}
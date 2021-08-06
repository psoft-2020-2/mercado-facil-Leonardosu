package com.ufcg.psoft.mercadofacil.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufcg.psoft.mercadofacil.model.Carrinho;
import com.ufcg.psoft.mercadofacil.model.Compra;
import com.ufcg.psoft.mercadofacil.repository.CarrinhoRepository;

@Service
public class CarrinhoServiceImpl implements CarrinhoService {

    @Autowired
    private CarrinhoRepository carrinho;

    public void salvarCompra(Compra compra) {

        // carrinho.save(compra);
    }

    public List<Carrinho> listarCompras() {
        return carrinho.findAll();
    }

    // public Optional<Carrinho> getByIdDoProduto(long idDoProduto) {
    // return carrinho.findByIdDoProduto(idDoProduto);
    // }

    @Override
    public void salvarCarrinho(Carrinho carrinho) {
        // TODO Auto-generated method stub
        // carrinho.save(carrinho);
    }

    @Override
    public void deletarCarrinho(Carrinho carrinho) {
        // TODO Auto-generated method stub

    }

}
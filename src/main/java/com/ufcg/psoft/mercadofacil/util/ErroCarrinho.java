package com.ufcg.psoft.mercadofacil.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ufcg.psoft.mercadofacil.model.Produto;

public class ErroCarrinho {

    static final String CARRINHO_VAZIO = "O carrinho esta vazio";
    static final String PRODUTO_INVALIDO = "O produto %s nao esta no carrinho";
    static final String ESTOQUE_INSUFICIENTE = "Nao ha estoque o suficiente do produto %s";

    public static ResponseEntity<CustomErrorType> erroCarrinhoVazio() {
        return new ResponseEntity<CustomErrorType>(new CustomErrorType(ErroCarrinho.CARRINHO_VAZIO),
                HttpStatus.NOT_ACCEPTABLE);
    }

    public static ResponseEntity<CustomErrorType> erroEstoqueInsuficiente(Produto produto) {
        return new ResponseEntity<CustomErrorType>(
                new CustomErrorType(String.format(ErroCarrinho.ESTOQUE_INSUFICIENTE, produto.getNome())),
                HttpStatus.NOT_ACCEPTABLE);
    }

    public static ResponseEntity<CustomErrorType> erroProdutoNaoEncontrado(Produto produto) {
        return new ResponseEntity<CustomErrorType>(
                new CustomErrorType(String.format(ErroCarrinho.PRODUTO_INVALIDO, produto.getNome())),
                HttpStatus.NOT_ACCEPTABLE);
    }

}
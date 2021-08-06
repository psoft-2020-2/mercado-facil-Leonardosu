package com.ufcg.psoft.mercadofacil.util;

public class PagamentoInvalidoException extends Exception {
    public PagamentoInvalidoException(String erro) {
        super(erro);
    }
}

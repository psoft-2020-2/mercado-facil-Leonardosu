package com.ufcg.psoft.mercadofacil.model.Estrategias.PorProduto;

public class Comum implements EstrategiaProduto {

    @Override
    public double getValor(double valor) {
        return valor;
    }

}

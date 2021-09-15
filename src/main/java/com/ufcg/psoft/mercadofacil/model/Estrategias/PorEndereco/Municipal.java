package com.ufcg.psoft.mercadofacil.model.Estrategias.PorEndereco;

public class Municipal implements EstrategiaEndereco {

    /**
     * Preco normal.
     */
    @Override
    public double getValor(double valor) {
        return valor;
    }

}

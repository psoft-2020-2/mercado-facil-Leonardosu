package com.ufcg.psoft.mercadofacil.model.Estrategias.PorEndereco;

public class Bairro implements EstrategiaEndereco {

    /**
     * Desconto de 20%
     */
    @Override
    public double getValor(double valor) {
        return valor * (0.8);
    }

}

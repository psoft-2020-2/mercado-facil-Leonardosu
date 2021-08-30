package com.ufcg.psoft.mercadofacil.model.Estrategias.PorEndereco;

public class Estadual implements EstrategiaEndereco {

    /**
     * Aumento de 20%
     */
    @Override
    public double getValor(double valor) {
        return valor * (1.2);
    }

}

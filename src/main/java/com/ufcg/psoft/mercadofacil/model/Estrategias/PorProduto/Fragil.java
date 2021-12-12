package com.ufcg.psoft.mercadofacil.model.Estrategias.PorProduto;

public class Fragil implements EstrategiaProduto {

    /**
     * Aumento de 10%
     */
    @Override
    public double getValor(double valor) {
        return valor * (1.1);
    }

}

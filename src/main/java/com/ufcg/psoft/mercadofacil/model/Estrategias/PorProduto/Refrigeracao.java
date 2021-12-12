package com.ufcg.psoft.mercadofacil.model.Estrategias.PorProduto;

public class Refrigeracao implements EstrategiaProduto {

    /**
     * Aumento de 30%
     */
    @Override
    public double getValor(double valor) {
        return valor * (1.3);
    }

}

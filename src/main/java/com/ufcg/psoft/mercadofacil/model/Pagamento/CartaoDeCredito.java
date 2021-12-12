package com.ufcg.psoft.mercadofacil.model.Pagamento;

public class CartaoDeCredito extends Pagamento {

    /**
     * O pagamento em cartão de crédito há um acréscimo de 5% no valor da compra.
     * 
     * @param valor valor da compra
     * @return valor final
     */
    @Override
    public double getValor(double valor) {
        return valor * 1.05;
    }
}

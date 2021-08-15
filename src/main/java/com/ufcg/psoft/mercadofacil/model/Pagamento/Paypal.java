package com.ufcg.psoft.mercadofacil.model.Pagamento;

public class Paypal extends Pagamento {
    /**
     * O pagamento em paypal há um acréscimo de 2% no valor da compra.
     * 
     * @param valor valor da compra
     * @return valor final
     */
    @Override
    public double getValor(double valor) {
        return valor * 1.02;
    }
}

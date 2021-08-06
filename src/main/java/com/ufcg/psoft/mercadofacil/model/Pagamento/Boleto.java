package com.ufcg.psoft.mercadofacil.model.Pagamento;

public class Boleto extends Pagamento {

    /**
     * O pagamento via boleto não há acréscimo no valor da compra.
     * 
     * @param valor valor da compra
     * @return valor final
     */
    @Override
    public double getValor(double valor) {
        return valor;
    }
}

package com.ufcg.psoft.mercadofacil.model.Entrega;

public class Retirada extends Entrega {

   /**
    * Reduz em 10%
    */
   @Override
   public double getValor(double valor) {

      return valor * (0.9);
   }

}

package com.ufcg.psoft.mercadofacil.model.Entrega;

public class Express extends Entrega {

   /**
    * Aumenta em 10%
    */
   @Override
   public double getValor(double valor) {
      return valor * (1.1);
   }

}

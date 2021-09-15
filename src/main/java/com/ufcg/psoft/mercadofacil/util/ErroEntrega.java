package com.ufcg.psoft.mercadofacil.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErroEntrega {

    static final String ENTREGA_INVALIDA = "Forma de entrega invalida";

    public static ResponseEntity<CustomErrorType> erroEntregaInvalida() {

        return new ResponseEntity<CustomErrorType>(new CustomErrorType(String.format(ErroEntrega.ENTREGA_INVALIDA)),
                HttpStatus.NOT_ACCEPTABLE);
    }
}

package com.ufcg.psoft.mercadofacil.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErroPagamento {

    static final String PAGAMENTO_INVALIDO = "Pagamento invalido";

    static final String OPCOES_DE_PAGAMENTO = "Boleto\nPayPal\nCartaoDeCredito\n";

    public static ResponseEntity<CustomErrorType> erroLoteNaoEncontrado(long id) {
        return new ResponseEntity<CustomErrorType>(new CustomErrorType(String.format(ErroLote.LOTE_NAO_CASTRADO, id)),
                HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity<CustomErrorType> erroSemLotesCadastrados() {
        return new ResponseEntity<CustomErrorType>(new CustomErrorType(ErroLote.SEM_LOTES_CADASTRADOS),
                HttpStatus.NO_CONTENT);
    }

    public static ResponseEntity<CustomErrorType> erroPagamentoInvalido() {

        return new ResponseEntity<CustomErrorType>(
                new CustomErrorType(String.format(ErroPagamento.PAGAMENTO_INVALIDO, ErroPagamento.OPCOES_DE_PAGAMENTO)),
                HttpStatus.NOT_ACCEPTABLE);
    }
}

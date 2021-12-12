package com.ufcg.psoft.mercadofacil.DTO;

public class CompraDTO {

    private String pagamento;

    CompraDTO(String pagamento) {
        this.pagamento = pagamento;
    }

    public void setPagamento(String pagamento) {
        this.pagamento = pagamento;
    }

    public String getPagamento() {
        return this.pagamento;
    }
}

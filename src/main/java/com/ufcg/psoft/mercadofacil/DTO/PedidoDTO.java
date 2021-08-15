package com.ufcg.psoft.mercadofacil.DTO;

public class PedidoDTO {

    private long idProduto;
    private long quantidade;

    PedidoDTO() {
    }

    PedidoDTO(long idProduto, long quantidade) {
        this.idProduto = idProduto;
        this.quantidade = quantidade;
    }

    public long getIdProduto() {
        return this.idProduto;
    }

    public void setIdProduto(long id) {
        this.idProduto = id;
    }

    public long getQuantidade() {
        return this.quantidade;
    }

    public void setQuantidade(long novaQuantidade) {
        this.quantidade = novaQuantidade;
    }

}

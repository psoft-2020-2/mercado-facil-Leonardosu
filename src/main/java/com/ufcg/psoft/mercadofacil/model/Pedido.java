package com.ufcg.psoft.mercadofacil.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private Produto produto;

    @ManyToOne
    private Carrinho carrinho;

    private long quantidade;

    public Pedido() {

    }

    /**
     * 
     * @param id         Id da compra
     * @param produto    Produto
     * @param quantidade quantidade de produtos.
     */
    public Pedido(Produto produto, long quantidade) {
        // this.id = id;
        super();
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public long getId() {
        return this.id;
    }

    public Produto getProduto() {
        return this.produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public long getQuantidade() {
        return this.quantidade;
    }

    public void setQuantidade(long quantidade) {
        this.quantidade = quantidade;
    }

    public void setCarrinho(Carrinho carrinho) {
        this.carrinho = carrinho;
    }

    public BigDecimal getPreco() {
        return this.produto.getPreco();
    }

    @Override
    public String toString() {
        return produto.getNome();
    }

}
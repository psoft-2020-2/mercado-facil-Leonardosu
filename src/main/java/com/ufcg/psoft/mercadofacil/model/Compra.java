package com.ufcg.psoft.mercadofacil.model;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

// import org.hibernate.internal.util.type.PrimitiveWrapperHelper.LongDescriptor;

@Entity
public class Compra {

    @Id
    @GeneratedValue
    private Long id;
    private Long idDoProduto;
    private Long quantidade;

    public Compra() {
    }

    public Compra(long idDoProduto, long quantidade) {
        this.idDoProduto = idDoProduto;
        this.quantidade = quantidade;
    }

    public long getIdDoProduto() {
        return this.idDoProduto;
    }

    public long getQuantidade() {
        return this.quantidade;
    }

    public void setQuantidade(long novoValor) {
        this.quantidade = novoValor;
    }

    public int hashMap() {
        return Objects.hashCode(idDoProduto);
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        if (this.getIdDoProduto() != ((Compra) obj).getIdDoProduto())
            return false;

        return true;
    }

    public String toString() {
        return this.idDoProduto + " x " + this.quantidade;
    }

    public void addQuantidade(Compra compra) {
        this.quantidade += compra.getQuantidade();
    }

}
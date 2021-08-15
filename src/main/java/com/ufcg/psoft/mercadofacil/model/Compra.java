package com.ufcg.psoft.mercadofacil.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.ufcg.psoft.mercadofacil.model.Cliente.Cliente;
import com.ufcg.psoft.mercadofacil.util.PagamentoInvalidoException;

@Entity
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    /*
     * Um compra possui um ou mais produtos e eh realizada por um cliente.
     */
    @ManyToMany
    private Set<Produto> produtos = new HashSet<Produto>();

    @ManyToOne(cascade = CascadeType.ALL)
    private Cliente cliente;

    /**
     * US01: Como cliente do sistema Mercado Fácil, quero definir a forma de
     * pagamento (boleto, paypal e cartão de crédito) ao finalizar compras, para que
     * seja possível pagar as compras da forma mais conveniente.
     */
    public String pagamento;
    private BigDecimal valorTotal;
    private String data;

    public Compra() {
        super();
        valorTotal = new BigDecimal(0);
        this.data = gerarData();
    }

    /**
     * 
     * @param id        id da compra
     * @param data      data da compra
     * @param pagamento forma de pagamento
     * @param produtos  Conjunto de produtos que foi comprado
     * @param valor     valor total da compra
     */

    public Compra(Long id, String data, String pagamento, Set<Produto> produtos, BigDecimal valor) {
        super();
        this.id = id;
        this.data = data;
        this.pagamento = pagamento;
        this.produtos = produtos;
        this.valorTotal = valor;
    }

    public Long getId() {
        return this.id;
    }

    public String getPagamento() {
        return this.pagamento;
    }

    // throws PagamentoInvalidoException
    public void setPagamento(String pagamento) {

        // pagamento = pagamento.toLowerCase();
        this.pagamento = pagamento;
        // if (pagamento == "boleto" || pagamento == "paypal" || pagamento ==
        // "cartaocredito")
        // this.pagamento = pagamento;
        // else
        // throw new PagamentoInvalidoException("Forma de Pagamento Invalida");

        // if (pagamento == "Boleto")
        // this.pagamento = Compra.BOLETO;
        // else if (pagamento == 2)
        // this.pagamento = Compra.PAYPAL;
        // else if (pagamento == 3)
        // this.pagamento = Compra.CARTAO;
    }

    public BigDecimal getValor() {
        return this.valorTotal;
    }

    public void setValor(BigDecimal valor) {
        this.valorTotal = valor;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String gerarData() {
        // SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date data = new Date();
        return data.toString();
    }

    public Set<Produto> getProdutos() {
        return this.produtos;
    }

    public void setProdutos(Set<Produto> produtos) {
        this.produtos = produtos;
    }

    public Set<Produto> adicionaProduto(Produto produto) {
        this.produtos.add(produto);
        return this.produtos;
    }

    public String stringPagamento(int pagamento) {
        if (pagamento == 1)
            return "BOLETO";

        if (pagamento == 2)
            return "PAYPAL";

        if (pagamento == 3)
            return "CARTAO";

        return "invalido";
    }

    @Override
    public String toString() {
        return this.id + " " + "Data: " + getData() + "\n" + "Valor: " + getValor().toString() + "\n" + "Produtos: "
                + getProdutos();
    }

}
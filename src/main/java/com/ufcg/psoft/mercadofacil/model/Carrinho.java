package com.ufcg.psoft.mercadofacil.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.ufcg.psoft.mercadofacil.model.Cliente.Cliente;

@Entity
public class Carrinho {

    @Id
    private long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pedido> pedidos = new LinkedList<Pedido>();

    @OneToOne
    @MapsId
    private Cliente cliente;

    public Carrinho() {
        super();
    }

    public Carrinho(long id) {
        super();
        this.id = id;
    }

    public List<Pedido> getPedidos() {
        return this.pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos.clear();
        this.pedidos.addAll(pedidos);
    }

    public List<Pedido> adicionaPedido(Pedido pedido) {

        if (!findPedido(pedido))
            this.pedidos.add(pedido);
        else {

            for (int i = 0; i < pedidos.size(); ++i) {
                if (pedidos.get(i).getId() == pedido.getId()) {
                    pedidos.set(i, pedido);
                    break;
                }
            }
        }

        return this.pedidos;
    }

    public double calculaTotal() {
        double total = 0;
        for (Pedido p : this.pedidos) {
            double preco = p.getProduto().getPreco().doubleValue();
            total = preco * p.getQuantidade();
        }
        return total;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public boolean findPedido(Pedido pedido) {
        for (Pedido pedidoAtual : pedidos)
            if (pedidoAtual.getId() == pedido.getId())
                return true;

        return false;
    }

    public void removePedido(Pedido pedido) {

        List<Pedido> novaLista = new ArrayList<Pedido>();

        for (Pedido pedidoAtual : pedidos) {
            if (pedidoAtual.getId() != pedido.getId())
                novaLista.add(pedidoAtual);
        }

        this.pedidos = novaLista;
    }

    public long getQuantidadeTotal() {
        long quantidade = 0;
        for (Pedido pedidoAtual : pedidos)
            quantidade += pedidoAtual.getQuantidade();

        return quantidade;
    }

    public boolean isEmpty() {
        return this.pedidos.size() == 0;
    }
}

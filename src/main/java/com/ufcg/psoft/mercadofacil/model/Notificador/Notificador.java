package com.ufcg.psoft.mercadofacil.model.Notificador;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;

import com.ufcg.psoft.mercadofacil.model.Cliente.Cliente;
import com.ufcg.psoft.mercadofacil.model.Produto;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Notificador {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne
    private Produto produto;

    @ManyToMany
    private Set<Cliente> clientes;

    private String tipo;

    Notificador() {
        this.clientes = new HashSet<Cliente>();
    }

    Notificador(Produto produto) {
        this.clientes = new HashSet<Cliente>();
        this.produto = produto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Produto getProduto() {
        return this.produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Set<Cliente> getClientes() {
        return this.clientes;
    }

    public void adicionaCliente(Cliente cliente) {
        this.clientes.add(cliente);
    }

    public void removeCliente(Long idProduto, Cliente cliente) {
        this.clientes.remove(cliente);
    }

    public abstract String notificaCliente();

}
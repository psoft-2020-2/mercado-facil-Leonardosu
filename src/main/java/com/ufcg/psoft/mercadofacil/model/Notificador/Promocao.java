package com.ufcg.psoft.mercadofacil.model.Notificador;

import javax.persistence.Entity;
import javax.persistence.Inheritance;

import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.model.Cliente.Cliente;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Promocao extends Notificador {
    Promocao() {
        super();
    }

    public Promocao(Produto produto) {
        super(produto);
        super.setTipo("promocao");
    }

    @Override
    public String notificaCliente() {
        Produto produto = super.getProduto();
        String resultado = produto.getNome() + " id: " + produto.getId() + " esta na promocao.\n";
        resultado += "Clientes que estavam esperando : \n";

        for (Cliente cliente : super.getClientes())
            resultado += cliente.getNome() + "\n";

        return resultado;
    }

}

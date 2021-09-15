package com.ufcg.psoft.mercadofacil.model.Notificador;

import javax.persistence.Entity;
import javax.persistence.Inheritance;

import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.model.Cliente.Cliente;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class NovoEstoque extends Notificador {
    NovoEstoque() {
        super();
    }

    public NovoEstoque(Produto produto) {
        super(produto);
        super.setTipo("estoque");
    }

    @Override
    public String notificaCliente() {
        Produto produto = super.getProduto();
        String resultado = produto.getNome() + " " + produto.getId() + " esta disponivel.\n";
        resultado += "Clientes que estavam esperando : \n";

        for (Cliente cliente : super.getClientes())
            resultado += cliente.getNome() + "\n";

        return resultado;
    }

}

package com.ufcg.psoft.mercadofacil.model.Cliente;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.ufcg.psoft.mercadofacil.DTO.ClienteDTO;

@Entity
@DiscriminatorValue(value = "Premium")
public class ClientePremium extends Cliente {
    public ClientePremium() {
        super();
        // super.setTipo("Premium");
    }

    public ClientePremium(long id) {
        super();
        super.setId(id);
    }

    public ClientePremium(ClienteDTO clienteDTO) {
        super(clienteDTO);
    }

    public ClientePremium(Cliente novoPerfil) {
        super(novoPerfil);
    }

    @Override
    public double descontoCompras(double valor, long quantidade) {
        if (quantidade > 5)
            return valor * 0.9;
        else
            return valor;
    }

    @Override
    public String toString() {
        return "Premium";
    }
}

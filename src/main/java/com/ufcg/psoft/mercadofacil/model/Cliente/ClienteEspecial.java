package com.ufcg.psoft.mercadofacil.model.Cliente;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.ufcg.psoft.mercadofacil.DTO.ClienteDTO;

@Entity
@DiscriminatorValue(value = "Especial")
public class ClienteEspecial extends Cliente {

    public ClienteEspecial() {
        super();
    }

    public ClienteEspecial(long id) {
        super();
        super.setId(id);
    }

    public ClienteEspecial(ClienteDTO clienteDTO) {
        super(clienteDTO);
    }

    public ClienteEspecial(Cliente novoPerfil) {
        super(novoPerfil);
    }

    @Override
    public double descontoCompras(double valor, long quantidade) {

        if (quantidade > 10)
            return valor * 0.9;
        else
            return valor;
    }

    @Override
    public String toString() {
        return "Especial";
    }
}

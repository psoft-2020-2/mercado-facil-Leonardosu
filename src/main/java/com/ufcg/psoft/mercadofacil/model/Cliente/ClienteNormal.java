package com.ufcg.psoft.mercadofacil.model.Cliente;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.ufcg.psoft.mercadofacil.DTO.ClienteDTO;

@Entity
@DiscriminatorValue(value = "Normal")
public class ClienteNormal extends Cliente {

    public ClienteNormal() {
        super();
        // super.setTipo("Normal");
    }

    public ClienteNormal(long id) {
        super();
        super.setId(id);
    }

    public ClienteNormal(ClienteDTO clienteDTO) {
        super(clienteDTO);
    }

    public ClienteNormal(Cliente novoPerfil) {
        super(novoPerfil);
    }

    @Override
    public double descontoCompras(double valor, long quantidade) {
        return valor;
    }

    @Override
    public String toString() {
        return "Normal";
    }
}

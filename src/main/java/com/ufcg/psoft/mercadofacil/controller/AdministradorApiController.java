package com.ufcg.psoft.mercadofacil.controller;

import java.util.List;
import java.util.Optional;
import com.ufcg.psoft.mercadofacil.model.Compra;
import com.ufcg.psoft.mercadofacil.model.Carrinho;
import com.ufcg.psoft.mercadofacil.model.Cliente.Cliente;
import com.ufcg.psoft.mercadofacil.model.Cliente.ClienteEspecial;
import com.ufcg.psoft.mercadofacil.model.Cliente.ClienteNormal;
import com.ufcg.psoft.mercadofacil.model.Cliente.ClientePremium;
import com.ufcg.psoft.mercadofacil.repository.CarrinhoRepository;
import com.ufcg.psoft.mercadofacil.repository.ClienteRepository;
import com.ufcg.psoft.mercadofacil.util.ErroCliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class AdministradorApiController {

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    CarrinhoRepository carrinhoRepository;

    @RequestMapping(value = "cliente/{id}/especial", method = RequestMethod.PUT)
    public ResponseEntity<?> ClienteParaEspecial(@PathVariable("id") long id) {

        Optional<Cliente> clienteOP = clienteRepository.findById(id);

        if (!clienteOP.isPresent()) {
            return ErroCliente.erroClienteNaoEnconrtrado(id);
        }

        Cliente cliente = clienteOP.get();

        if (cliente.toString() == "Especial") {
            return ErroCliente.erroClienteJaEhEspecial();
        }

        Carrinho carrinho = cliente.getCarrinho();
        List<Compra> compras = cliente.getCompras();

        if (cliente.getCarrinho() == null) {

            carrinho = new Carrinho();

            carrinho.setCliente(cliente);

            // clienteRepository.save(cliente);
            carrinhoRepository.save(carrinho);
        }

        // Cliente clienteEspecial =
        Cliente clienteEspecial = new ClienteEspecial(cliente);

        clienteRepository.deleteById(id);
        clienteEspecial.setId(id);

        clienteRepository.save(clienteEspecial);

        return new ResponseEntity<Cliente>(clienteEspecial, HttpStatus.OK);
    }

    @RequestMapping(value = "cliente/{id}/premium", method = RequestMethod.PUT)
    public ResponseEntity<?> ClienteParaPremium(@PathVariable("id") long id) {

        Optional<Cliente> clienteOP = clienteRepository.findById(id);

        if (!clienteOP.isPresent()) {
            return ErroCliente.erroClienteNaoEnconrtrado(id);
        }

        Cliente cliente = clienteOP.get();

        if (cliente.toString() == "Premium") {
            return ErroCliente.erroClienteJaEhPremium();
        }

        Cliente clientePremium = new ClientePremium(cliente);
        // clientePremium.setTipo("Premium");

        clientePremium.setCompras(cliente.getCompras());
        if (cliente.getCarrinho() == null) {

            Carrinho carrinho = new Carrinho(cliente.getId());
            carrinho.setCliente(clientePremium);
            clientePremium.setCarrinho(carrinho);

            clienteRepository.save(clientePremium);
            carrinhoRepository.save(carrinho);
        } else
            clientePremium.setCarrinho(cliente.getCarrinho());

        clienteRepository.delete(cliente);
        clienteRepository.save(clientePremium);

        return new ResponseEntity<Cliente>(clientePremium, HttpStatus.OK);
    }

    @RequestMapping(value = "cliente/{id}/normal", method = RequestMethod.PUT)
    public ResponseEntity<?> ClienteParaNormal(@PathVariable("id") long id) {

        Optional<Cliente> clienteOP = clienteRepository.findById(id);

        if (!clienteOP.isPresent()) {
            return ErroCliente.erroClienteNaoEnconrtrado(id);
        }

        Cliente cliente = clienteOP.get();

        if (cliente.toString() == "Normal") {
            return ErroCliente.erroClienteJaEhNormal();
        }

        Cliente clienteNormal = new ClienteNormal(cliente);
        // clienteNormal.setTipo("Normal");
        clienteNormal.setCompras(cliente.getCompras());
        if (cliente.getCarrinho() == null) {

            Carrinho carrinho = new Carrinho(cliente.getId());
            carrinho.setCliente(clienteNormal);
            clienteNormal.setCarrinho(carrinho);

            clienteRepository.save(clienteNormal);
            carrinhoRepository.save(carrinho);
        } else
            clienteNormal.setCarrinho(cliente.getCarrinho());

        clienteRepository.delete(cliente);
        clienteRepository.save(clienteNormal);

        return new ResponseEntity<Cliente>(clienteNormal, HttpStatus.OK);
    }

}
package com.ufcg.psoft.mercadofacil.controller;

import java.util.Optional;

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

        if (!clienteOP.isPresent())
            return ErroCliente.erroClienteNaoEnconrtrado(id);

        Cliente cliente = clienteOP.get();

        if (cliente.toString() == "Especial")
            return ErroCliente.erroClienteJaEhEspecial();

        Cliente clienteEspecial = new ClienteEspecial(cliente);

        clienteEspecial.setCompras(cliente.getCompras());
        if (cliente.getCarrinho() == null)
            criarNovoCarrinho(cliente, clienteEspecial);
        else
            clienteEspecial.setCarrinho(cliente.getCarrinho());

        clienteRepository.deleteById(id);
        clienteEspecial.setId(id);

        clienteRepository.save(clienteEspecial);

        return new ResponseEntity<Cliente>(clienteEspecial, HttpStatus.OK);
    }

    @RequestMapping(value = "cliente/{id}/premium", method = RequestMethod.PUT)
    public ResponseEntity<?> ClienteParaPremium(@PathVariable("id") long id) {

        Optional<Cliente> clienteOP = clienteRepository.findById(id);

        if (!clienteOP.isPresent())
            return ErroCliente.erroClienteNaoEnconrtrado(id);

        Cliente cliente = clienteOP.get();

        if (cliente.toString() == "Premium")
            return ErroCliente.erroClienteJaEhPremium();

        Cliente clientePremium = new ClientePremium(cliente);
        clientePremium.setCompras(cliente.getCompras());

        if (cliente.getCarrinho() == null)
            criarNovoCarrinho(cliente, clientePremium);
        else
            clientePremium.setCarrinho(cliente.getCarrinho());

        clienteRepository.delete(cliente);
        clienteRepository.save(clientePremium);

        return new ResponseEntity<Cliente>(clientePremium, HttpStatus.OK);
    }

    @RequestMapping(value = "cliente/{id}/normal", method = RequestMethod.PUT)
    public ResponseEntity<?> ClienteParaNormal(@PathVariable("id") long id) {

        Optional<Cliente> clienteOP = clienteRepository.findById(id);

        if (!clienteOP.isPresent())
            return ErroCliente.erroClienteNaoEnconrtrado(id);

        Cliente cliente = clienteOP.get();

        if (cliente.toString() == "Normal")
            return ErroCliente.erroClienteJaEhNormal();

        Cliente clienteNormal = new ClienteNormal(cliente);
        clienteNormal.setCompras(cliente.getCompras());

        if (cliente.getCarrinho() == null) {
            criarNovoCarrinho(cliente, clienteNormal);
        } else
            clienteNormal.setCarrinho(cliente.getCarrinho());

        clienteRepository.delete(cliente);
        clienteRepository.save(clienteNormal);

        return new ResponseEntity<Cliente>(clienteNormal, HttpStatus.OK);
    }

    private void criarNovoCarrinho(Cliente cliente, Cliente novoCliente) {
        Carrinho carrinho = new Carrinho(cliente.getId());
        carrinho.setCliente(novoCliente);
        novoCliente.setCarrinho(carrinho);

        clienteRepository.save(novoCliente);
        carrinhoRepository.save(carrinho);
    }
}
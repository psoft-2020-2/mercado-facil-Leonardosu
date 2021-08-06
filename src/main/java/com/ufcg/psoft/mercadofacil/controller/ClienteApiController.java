package com.ufcg.psoft.mercadofacil.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.ufcg.psoft.mercadofacil.DTO.ClienteDTO;
import com.ufcg.psoft.mercadofacil.model.Carrinho;
import com.ufcg.psoft.mercadofacil.model.Compra;
import com.ufcg.psoft.mercadofacil.model.Cliente.Cliente;
import com.ufcg.psoft.mercadofacil.model.Cliente.ClienteNormal;
import com.ufcg.psoft.mercadofacil.repository.CarrinhoRepository;
import com.ufcg.psoft.mercadofacil.repository.ClienteRepository;
import com.ufcg.psoft.mercadofacil.repository.CompraRepository;
import com.ufcg.psoft.mercadofacil.service.ClienteService;
import com.ufcg.psoft.mercadofacil.util.ErroCliente;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ClienteApiController {

	@Autowired
	ClienteService clienteService;

	@Autowired
	ClienteRepository clienteRepository;

	@Autowired
	CarrinhoRepository carrinhoRepository;

	@Autowired
	CompraRepository compraRepository;

	@RequestMapping(value = "/clientes", method = RequestMethod.GET)
	public ResponseEntity<?> listarClientes() {

		List<Cliente> clientes = clienteService.listarClientes();

		if (clientes.isEmpty()) {
			return ErroCliente.erroSemClientesCadastrados();
		}

		return new ResponseEntity<List<Cliente>>(clientes, HttpStatus.OK);
	}

	@RequestMapping(value = "/cliente/", method = RequestMethod.POST)
	public ResponseEntity<?> criarCliente(@RequestBody ClienteDTO clienteDTO, UriComponentsBuilder ucBuilder) {

		Optional<Cliente> clienteOp = clienteService.getClienteByCPF(clienteDTO.getCPF());

		if (!clienteOp.isEmpty()) {
			return ErroCliente.erroClienteJaCadastrado(clienteDTO);
		}

		Cliente cliente = new ClienteNormal(clienteDTO);
		// clienteService.salvarClienteCadastrado(cliente);

		// Carrinho carrinho = new Carrinho(cliente.getId());
		// carrinho.setCliente(cliente);
		// carrinhoRepository.save(carrinho);
		// cliente.setCarrinho(carrinho);
		// clienteRepository.save(cliente);

		return new ResponseEntity<Cliente>(cliente, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/cliente/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> consultarCliente(@PathVariable("id") long id) {

		Optional<Cliente> clienteOp = clienteService.getClienteById(id);

		if (!clienteOp.isPresent()) {
			return ErroCliente.erroClienteNaoEnconrtrado(id);
		}

		return new ResponseEntity<Cliente>(clienteOp.get(), HttpStatus.OK);
	}

	@RequestMapping(value = "/cliente/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> atualizarCliente(@PathVariable("id") long id, @RequestBody ClienteDTO clienteDTO) {

		Optional<Cliente> clienteOp = clienteService.getClienteById(id);

		if (!clienteOp.isPresent()) {
			return ErroCliente.erroClienteNaoEnconrtrado(id);
		}

		Cliente cliente = clienteOp.get();

		clienteService.atualizaCliente(clienteDTO, cliente);
		clienteService.salvarClienteCadastrado(cliente);

		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
	}

	@RequestMapping(value = "/cliente/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> removerCliente(@PathVariable("id") long id) {

		Optional<Cliente> clienteOp = clienteService.getClienteById(id);

		if (!clienteOp.isPresent()) {
			return ErroCliente.erroClienteNaoEnconrtrado(id);
		}

		clienteService.removerClienteCadastrado(clienteOp.get());

		return new ResponseEntity<Cliente>(HttpStatus.OK);
	}

	@RequestMapping(value = "/cliente/{id}/compras", method = RequestMethod.GET)
	public ResponseEntity<?> listarCompras(@PathVariable("id") long id) {

		Optional<Cliente> clienteOP = clienteRepository.findById(id);

		if (!clienteOP.isPresent()) {
			return ErroCliente.erroClienteNaoEnconrtrado(id);
		}

		List<Compra> compras = new ArrayList<Compra>();
		compras = compraRepository.findCompraByClienteId(id);

		if (compras.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		Cliente cliente = clienteOP.get();

		List<String> simples = cliente.compraSimples(compras);

		return new ResponseEntity<List<String>>(simples, HttpStatus.OK);
	}

	@RequestMapping(value = "/cliente/{id}/compra/{idCompra}", method = RequestMethod.GET)
	public ResponseEntity<?> consultarCompra(@PathVariable("id") long id, @PathVariable("idCompra") long idCompra) {

		Optional<Cliente> clienteOP = clienteRepository.findById(id);
		Optional<Compra> compraOP = compraRepository.findById(idCompra);

		if (!clienteOP.isPresent())
			return ErroCliente.erroClienteNaoEnconrtrado(id);

		if (!compraOP.isPresent())
			return ErroCliente.erroSemClientesCadastrados();

		Compra compra = compraOP.get();

		List<Compra> compras = new ArrayList<Compra>();
		compras = compraRepository.findCompraByClienteId(id);

		if (compras.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<Compra>(compra, HttpStatus.OK);
	}
}
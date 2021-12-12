package com.ufcg.psoft.mercadofacil.controller;

import com.ufcg.psoft.mercadofacil.model.Cliente.Cliente;
import java.util.Set;

import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.ClienteRepository;
import com.ufcg.psoft.mercadofacil.repository.ProdutoRepository;
import com.ufcg.psoft.mercadofacil.service.NotificadorServiceImpl;
import com.ufcg.psoft.mercadofacil.util.ErroCliente;
import com.ufcg.psoft.mercadofacil.util.ErroProduto;
import com.ufcg.psoft.mercadofacil.repository.NotificadorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class NotificacaoApiController {
    static final String ESTOQUE = "estoque";
    static final String PROMOCAO = "promocao";

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    ProdutoRepository produtoRepository;

    @Autowired
    NotificadorRepository notificadorRepository;

    @Autowired
    NotificadorServiceImpl notificadorServiceImpl;

    @RequestMapping(value = "/listaEstoque/produto/{idProduto}/cliente/", method = RequestMethod.POST)
    public ResponseEntity<?> cadastraListaEspera(@PathVariable("idProduto") long idProduto,
            @RequestParam long idCliente) {

        if (!clienteRepository.findById(idCliente).isPresent())
            return ErroCliente.erroClienteNaoEnconrtrado(idCliente);
        if (!produtoRepository.findById(idProduto).isPresent())
            return ErroProduto.erroProdutoNaoEnconrtrado(idProduto);

        Produto produto = produtoRepository.findById(idProduto).get();
        if (produto.getEstoque() != 0)
            return ErroProduto.erroQuantidadePositiva();

        Cliente cliente = clienteRepository.findClienteById(idCliente);

        notificadorServiceImpl.salvarClienteNotificador("estoque", produto, cliente);

        String resultado = "Cliente " + cliente.getNome() + " esta aguardando o novo lote de " + produto.getNome();
        return new ResponseEntity<>(resultado, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/listaPromocao/produto/{idProduto}/cliente/", method = RequestMethod.POST)
    public ResponseEntity<?> cadastraListaPromocao(@PathVariable("idProduto") long idProduto,
            @RequestParam long idCliente) {

        if (!clienteRepository.findById(idCliente).isPresent())
            return ErroCliente.erroClienteNaoEnconrtrado(idCliente);
        if (!produtoRepository.findById(idProduto).isPresent())
            return ErroProduto.erroProdutoNaoEnconrtrado(idProduto);

        Produto produto = produtoRepository.findById(idProduto).get();
        if (produto.getEstoque() != 0)
            return ErroProduto.erroQuantidadePositiva();

        Cliente cliente = clienteRepository.findClienteById(idCliente);
        notificadorServiceImpl.salvarClienteNotificador("promocao", produto, cliente);

        String resultado = "Cliente " + cliente.getNome() + " esta aguardando abaixar o preco de " + produto.getNome();
        return new ResponseEntity<>(resultado, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/listaEstoque/produto/{idProduto}/", method = RequestMethod.GET)
    public ResponseEntity<?> ListaEspera(@PathVariable("idProduto") long idProduto) {
        if (!produtoRepository.findById(idProduto).isPresent())
            return ErroProduto.erroProdutoNaoEnconrtrado(idProduto);
        Produto produto = produtoRepository.findById(idProduto).get();
        if (produto.getEstoque() != 0)
            return ErroProduto.erroQuantidadePositiva();

        Set<Cliente> clientes = notificadorServiceImpl.listarClienteNotificadorByProduto("estoque", produto);
        if (clientes.isEmpty())
            return new ResponseEntity<>("Lista vazia.", HttpStatus.OK);

        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    @RequestMapping(value = "/listaPromocao/produto/{idProduto}/", method = RequestMethod.GET)
    public ResponseEntity<?> ListaPromocao(@PathVariable("idProduto") long idProduto) {
        if (!produtoRepository.findById(idProduto).isPresent())
            return ErroProduto.erroProdutoNaoEnconrtrado(idProduto);
        Produto produto = produtoRepository.findById(idProduto).get();
        if (produto.getEstoque() != 0)
            return ErroProduto.erroQuantidadePositiva();

        Set<Cliente> clientes = notificadorServiceImpl.listarClienteNotificadorByProduto("promocao", produto);
        if (clientes.isEmpty())
            return new ResponseEntity<>("Lista vazia.", HttpStatus.OK);
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }
}

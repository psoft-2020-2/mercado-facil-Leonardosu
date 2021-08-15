package com.ufcg.psoft.mercadofacil.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

import com.ufcg.psoft.mercadofacil.DTO.PedidoDTO;

import com.ufcg.psoft.mercadofacil.model.*;
import com.ufcg.psoft.mercadofacil.model.Cliente.*;
import com.ufcg.psoft.mercadofacil.model.Pagamento.Boleto;
import com.ufcg.psoft.mercadofacil.model.Pagamento.CartaoDeCredito;
import com.ufcg.psoft.mercadofacil.model.Pagamento.Pagamento;
import com.ufcg.psoft.mercadofacil.model.Pagamento.Paypal;
import com.ufcg.psoft.mercadofacil.repository.*;
import com.ufcg.psoft.mercadofacil.service.ProdutoService;
import com.ufcg.psoft.mercadofacil.util.ErroCarrinho;
import com.ufcg.psoft.mercadofacil.util.ErroCliente;
import com.ufcg.psoft.mercadofacil.util.ErroProduto;
import com.ufcg.psoft.mercadofacil.util.ErroPagamento;

@RestController
@RequestMapping("/api")
@CrossOrigin

public class CarrinhoApiController {

    @Autowired
    PedidoRepository pedidoRepository;
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    CarrinhoRepository carrinhoRepository;
    @Autowired
    ProdutoRepository produtoRepository;
    @Autowired
    LoteRepository loteRepository;
    @Autowired
    CompraRepository compraRepository;

    @Autowired
    ProdutoService produtoService;

    @RequestMapping(value = "/cliente/{id}/pedido", method = RequestMethod.POST)
    public ResponseEntity<?> adicionarProduto(@PathVariable("id") long id, @RequestBody PedidoDTO pedidoDTO) {

        Optional<Produto> produtoOP = produtoRepository.findById(pedidoDTO.getIdProduto());
        Cliente cliente = clienteRepository.findClienteById(id);

        // Cliente não encontrado.
        if (!clienteRepository.findById(id).isPresent()) {
            return ErroCliente.erroClienteNaoEnconrtrado(id);
        }
        // Produto não encontrado.
        if (!produtoOP.isPresent()) {
            return ErroProduto.erroProdutoNaoEnconrtrado(pedidoDTO.getIdProduto());
        }
        // Quantidade negativas ou nulas de produtos
        if (pedidoDTO.getQuantidade() <= 0) {
            return ErroProduto.erroQuantidadeInvalida(pedidoDTO.getQuantidade());
        }
        // Quantidade maior que estoque atual do produto
        // Falta checar se, a quantidade no carrinho + qnt a ser adicionado <= estoque,
        // mais pra frente.
        if (pedidoDTO.getQuantidade() > produtoOP.get().getEstoque())
            return ErroProduto.erroEstoqueInsuficiente();

        Carrinho carrinho = cliente.getCarrinho();
        // Caso de borda, usando data.sql o carrinho inicial como null
        if (carrinho == null) {

            carrinho = new Carrinho(cliente.getId());
            carrinho.setCliente(cliente);
            cliente.setCarrinho(carrinho);

            clienteRepository.save(cliente);
            carrinhoRepository.save(carrinho);
        }

        Produto produto = produtoOP.get();
        Pedido pedido = new Pedido(produto, pedidoDTO.getQuantidade());
        // Caso já exista tal produto no carrinho, aumento a quantidade atual
        Pedido pedidoExistente = pedidoRepository.findPedidoByProdutoId(produto.getId());

        if (carrinho.findPedido(pedido)) {

            long qntdCarrinho = pedidoDTO.getQuantidade() + pedidoExistente.getQuantidade();

            if (qntdCarrinho > produto.getEstoque())
                return ErroCarrinho.erroEstoqueInsuficiente(produto);

            pedidoExistente.setQuantidade(qntdCarrinho);
            pedidoRepository.save(pedidoExistente);
            return new ResponseEntity<>(pedidoExistente, HttpStatus.OK);
        }

        pedido.setCarrinho(carrinho);
        pedidoRepository.save(pedido);

        carrinho.adicionaPedido(pedido);
        carrinhoRepository.save(carrinho);

        return new ResponseEntity<Carrinho>(carrinho, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/cliente/{id}/pedido", method = RequestMethod.PUT)
    public ResponseEntity<?> removeProduto(@PathVariable("id") long id, @RequestBody PedidoDTO pedidoDTO) {

        Optional<Produto> produtoOP = produtoRepository.findById(pedidoDTO.getIdProduto());
        Cliente cliente = clienteRepository.findClienteById(id);

        // Cliente não encontrado.
        if (!clienteRepository.findById(id).isPresent()) {
            return ErroCliente.erroClienteNaoEnconrtrado(id);
        }
        // Produto não encontrado.
        if (!produtoOP.isPresent()) {
            return ErroProduto.erroProdutoNaoEnconrtrado(pedidoDTO.getIdProduto());
        }
        // Quantidade negativas ou nulas de produtos
        if (pedidoDTO.getQuantidade() <= 0) {
            return ErroProduto.erroQuantidadeInvalida(pedidoDTO.getQuantidade());
        }

        Carrinho carrinho = cliente.getCarrinho();
        Produto produto = produtoOP.get();

        if (carrinho == null) {
            return ErroCarrinho.erroProdutoNaoEncontrado(produto);
        }

        // Pedido pedido = new Pedido(produto, pedidoDTO.getQuantidade());
        Pedido pedidoExistente = pedidoRepository.findPedidoByProdutoId(produto.getId());

        // Produto não esta no carrinho
        if (pedidoExistente == null)
            return ErroCarrinho.erroProdutoNaoEncontrado(produto);
        else {
            // Caso já exista tal produto no carrinho, diminuo a quantidade atual
            long qntdCarrinho = pedidoExistente.getQuantidade();
            long qntRemovida = Math.min(pedidoDTO.getQuantidade(), pedidoExistente.getQuantidade());

            pedidoExistente.setQuantidade(qntdCarrinho - qntRemovida);

            carrinho.removePedido(pedidoExistente);

            if (pedidoExistente.getQuantidade() > 0) {
                pedidoRepository.save(pedidoExistente);
                carrinho.adicionaPedido(pedidoExistente);
            }

            carrinhoRepository.save(carrinho);
        }

        return new ResponseEntity<>(pedidoExistente, HttpStatus.OK);
    }

    @RequestMapping(value = "/cliente/{id}/pedidos", method = RequestMethod.DELETE)
    public ResponseEntity<?> desfazCarrinho(@PathVariable("id") long id) {
        Cliente cliente = clienteRepository.findClienteById(id);

        if (cliente == null) {
            return ErroCliente.erroClienteNaoEnconrtrado(id);
        }

        carrinhoRepository.deleteById(id);

        Carrinho novoCarrinho = new Carrinho(id);
        cliente.setCarrinho(novoCarrinho);
        novoCarrinho.setCliente(cliente);

        clienteRepository.save(cliente);
        carrinhoRepository.save(novoCarrinho);

        return new ResponseEntity<>(novoCarrinho, HttpStatus.OK);
    }

    @RequestMapping(value = "/cliente/{id}/compra", method = RequestMethod.POST)
    public ResponseEntity<?> comprarCarrinho(@PathVariable("id") long id, @RequestBody String formaPagamento) {

        if (!clienteRepository.findById(id).isPresent()) {
            return ErroCliente.erroClienteNaoEnconrtrado(id);
        }

        Cliente cliente = clienteRepository.findClienteById(id);
        Carrinho carrinho = cliente.getCarrinho();

        if (carrinho == null || carrinho.getQuantidadeTotal() == 0) {
            return ErroCarrinho.erroCarrinhoVazio();
        }

        Compra compra = new Compra();
        Pagamento pagamento;
        formaPagamento = formaPagamento.toLowerCase();
        formaPagamento.equals("boleto");

        if (formaPagamento.equals("boleto")) {
            pagamento = new Boleto();
            compra.setPagamento("Boleto");
        } else if (formaPagamento.equals("paypal")) {
            pagamento = new Paypal();
            compra.setPagamento("PayPal");
        } else if (formaPagamento.equals("cartaodecredito")) {
            pagamento = new CartaoDeCredito();
            compra.setPagamento("CartaoDeCredito");
        } else
            return ErroPagamento.erroPagamentoInvalido();

        List<Pedido> pedidos = carrinho.getPedidos();

        long quantidade = carrinho.getQuantidadeTotal();
        BigDecimal valorTotal = new BigDecimal(0);

        for (Pedido pedidoAtual : pedidos) {

            BigDecimal quantidadeAtual = new BigDecimal(pedidoAtual.getQuantidade());
            BigDecimal valorAtual = pedidoAtual.getPreco().multiply(quantidadeAtual);
            valorTotal = valorTotal.add(valorAtual);

            long produtoId = pedidoAtual.getProduto().getId();

            Optional<Produto> produtoOP = produtoRepository.findById(produtoId);
            Produto produto = produtoOP.get();

            produto.removerEstoque(pedidoAtual.getQuantidade());
            produtoRepository.save(produto);
            compra.adicionaProduto(produto);
        }

        valorTotal = new BigDecimal(cliente.descontoCompras(valorTotal.doubleValue(), quantidade));

        valorTotal = new BigDecimal(pagamento.getValor(valorTotal.doubleValue()));

        compra.setValor(valorTotal.setScale(2, RoundingMode.HALF_UP));

        compra.setCliente(cliente);

        compraRepository.save(compra);

        cliente.adicionaCompra(compra);
        clienteRepository.save(cliente);

        this.desfazCarrinho(cliente.getId());

        return new ResponseEntity<String>(compra.toString(), HttpStatus.CREATED);
    }

}
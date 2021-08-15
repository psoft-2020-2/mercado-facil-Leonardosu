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

    @RequestMapping(value = "/cliente/{id}/compra", method = RequestMethod.GET)
    public ResponseEntity<?> listarFormasDePagamento() {

        String pagamentos = "Boleto\n" + "PayPal\n" + "CartaoDeCredito\n";

        return new ResponseEntity<String>(pagamentos, HttpStatus.OK);
    }

    @RequestMapping(value = "/cliente/{id}/compra", method = RequestMethod.POST)
    public ResponseEntity<?> comprarCarrinho(@PathVariable("id") long id, @RequestBody String formaPagamento) {

        if (!clienteRepository.findById(id).isPresent())
            return ErroCliente.erroClienteNaoEnconrtrado(id);

        Cliente cliente = clienteRepository.findClienteById(id);
        Carrinho carrinho = cliente.getCarrinho();

        if (carrinho == null || carrinho.getQuantidadeTotal() == 0)
            return ErroCarrinho.erroCarrinhoVazio();

        Compra compra = new Compra();
        List<Pedido> pedidos = carrinho.getPedidos();

        if (!pagamentoValido(formaPagamento))
            return ErroPagamento.erroPagamentoInvalido();

        Pagamento pagamento = criaPagamento(formaPagamento, compra);

        // Cálculo do valor final
        BigDecimal valorTotal = calculaPrecoCarrinho(pedidos, cliente, pagamento);
        // Atualização do estoque, removendo os produtos que estão no carrinho
        removeDoEstoque(pedidos);

        // Adicionando produtos que estão no carrinho a compra atual.
        adicionaPedidosNaCompra(pedidos, compra);
        compra.setValor(valorTotal.setScale(2, RoundingMode.HALF_UP));
        compra.setCliente(cliente);
        compraRepository.save(compra);

        cliente.adicionaCompra(compra);
        clienteRepository.save(cliente);

        this.desfazCarrinho(cliente.getId());

        return new ResponseEntity<String>(compra.toString(), HttpStatus.CREATED);
    }

    /**
     * Checa se dado a forma de pagamento ele é válido.
     * 
     * @param formaPagamento Forma de pagamento
     * @return
     */
    private boolean pagamentoValido(String formaPagamento) {
        if (formaPagamento == null)
            return false;

        formaPagamento = formaPagamento.toLowerCase().trim();
        if (formaPagamento.isEmpty() || formaPagamento.isBlank())
            return false;

        if (formaPagamento.equals("boleto") || formaPagamento.equals("paypal")
                || formaPagamento.equals("cartaodecredito"))
            return true;

        return false;
    }

    /**
     * Cria um pagamento de acordo com a forma de pagamento
     * 
     * @param formaPagamento Forma de pagamento
     * @param compra         Compra atual
     * @return
     */
    private Pagamento criaPagamento(String formaPagamento, Compra compra) {

        formaPagamento = formaPagamento.toLowerCase();
        Pagamento pagamento;

        if (formaPagamento.equals("boleto")) {
            pagamento = new Boleto();
            compra.setPagamento("Boleto");
        } else if (formaPagamento.equals("paypal")) {
            pagamento = new Paypal();
            compra.setPagamento("PayPal");
        } else {
            pagamento = new CartaoDeCredito();
            compra.setPagamento("CartaoDeCredito");
        }

        return pagamento;
    }

    /**
     * Método auxiliar para remover produtos do estoque dado uma lista de pedidos.
     * 
     * @param pedidos Lista de pedidos
     */
    private void removeDoEstoque(List<Pedido> pedidos) {
        for (Pedido pedidoAtual : pedidos) {
            long produtoId = pedidoAtual.getProduto().getId();
            Optional<Produto> produtoOP = produtoRepository.findById(produtoId);
            Produto produto = produtoOP.get();
            produto.removerEstoque(pedidoAtual.getQuantidade());
            produtoRepository.save(produto);
        }
    }

    /**
     * Método auxiliar para adicionar os pedidos em um compra.
     * 
     * @param pedidos Lista de pedidos.
     * @param compra  Compra, onde serão adicionado os pedidos
     */
    private void adicionaPedidosNaCompra(List<Pedido> pedidos, Compra compra) {
        for (Pedido pedidoAtual : pedidos) {
            long produtoId = pedidoAtual.getProduto().getId();
            Optional<Produto> produtoOP = produtoRepository.findById(produtoId);
            Produto produto = produtoOP.get();

            compra.adicionaProduto(produto);
        }
    }

    /**
     * Método auxiliar para calcular o preço dos itens que estão carrinho. Além de
     * calcular o desconto recebido de acordo com o tipo de Cliente e a quantidade
     * de itens no carrinho.
     * 
     * @param pedidos   Lista de pedidos que estão no carrinho
     * @param cliente   Cliente que irá efetuar a compra
     * @param pagamento Forma de pagamento
     * @return Valor final da compra.
     */
    private BigDecimal calculaPrecoCarrinho(List<Pedido> pedidos, Cliente cliente, Pagamento pagamento) {

        BigDecimal valorTotal = new BigDecimal(0);
        long quantidadeTotal = 0;

        for (Pedido pedidoAtual : pedidos) {
            quantidadeTotal += pedidoAtual.getQuantidade();

            BigDecimal quantidadeAtual = new BigDecimal(pedidoAtual.getQuantidade());
            BigDecimal valorAtual = pedidoAtual.getPreco().multiply(quantidadeAtual);
            valorTotal = valorTotal.add(valorAtual);
        }

        valorTotal = new BigDecimal(cliente.descontoCompras(valorTotal.doubleValue(), quantidadeTotal));
        valorTotal = new BigDecimal(pagamento.getValor(valorTotal.doubleValue()));

        return valorTotal;
    }
}
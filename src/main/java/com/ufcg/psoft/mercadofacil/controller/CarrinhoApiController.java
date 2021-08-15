package com.ufcg.psoft.mercadofacil.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ufcg.psoft.mercadofacil.model.Compra;
import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.service.CarrinhoService;
import com.ufcg.psoft.mercadofacil.service.ProdutoService;

import com.ufcg.psoft.mercadofacil.util.ErroCarrinho;
import com.ufcg.psoft.mercadofacil.util.ErroProduto;

@RestController
@RequestMapping("/api")
@CrossOrigin

public class CarrinhoApiController {

    @Autowired
    ProdutoService produtoService;

    @Autowired
    CarrinhoService carrinhoService;

    @RequestMapping(value = "/carrinho", method = RequestMethod.POST)
    public ResponseEntity<?> adicionarProduto(@RequestBody Compra compraDTO) {

        long idProduto = compraDTO.getIdDoProduto();

        Optional<Produto> produtoOP = produtoService.getProdutoById(idProduto);
        if (!produtoOP.isPresent() || !produtoOP.get().isDisponivel())
            return ErroProduto.erroProdutoNaoEnconrtrado(idProduto);

        Produto produto = produtoOP.get();

        if (produto.getEstoque() < compraDTO.getQuantidade())
            return ErroCarrinho.erroEstoqueInsuficiente(produto);

        Optional<Compra> comp = carrinhoService.getByIdDoProduto(idProduto);

        if (comp.isPresent()) {
            comp.get().addQuantidade(compraDTO);
            compraDTO = comp.get();
        }

        carrinhoService.salvarCompra(compraDTO);
        return new ResponseEntity<Compra>(compraDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/carrinho", method = RequestMethod.PUT)
    public ResponseEntity<?> removeProduto(@RequestBody Compra compraDTO) {

        long idProduto = compraDTO.getIdDoProduto();

        Optional<Produto> produtoOP = produtoService.getProdutoById(idProduto);
        if (!produtoOP.isPresent())
            return ErroProduto.erroProdutoNaoEnconrtrado(idProduto);

        Produto produto = produtoOP.get();

        Optional<Compra> compra = carrinhoService.getByIdDoProduto(idProduto);

        // Se nao tiver tal produto no carrinho
        if (!compra.isPresent())
            return ErroCarrinho.erroProdutoNaoEncontrado(produto);

        long qntdNoCarrinho = compra.get().getQuantidade();
        long qntdDesejado = compraDTO.getQuantidade();

        long novaQuantidade = Math.max(0, qntdNoCarrinho - qntdDesejado);

        compra.get().setQuantidade(novaQuantidade);
        compraDTO = compra.get();

        if (compraDTO.getQuantidade() == 0)
            carrinhoService.deletarCompra(compraDTO);
        else
            carrinhoService.salvarCompra(compraDTO);

        return new ResponseEntity<Compra>(compraDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/carrinho", method = RequestMethod.DELETE)
    public ResponseEntity<?> descartarCarrinho() {

        List<Compra> compras = carrinhoService.listarCompras();

        if (compras.isEmpty())
            return ErroCarrinho.erroCarrinhoVazio();

        for (Compra compra : compras)
            carrinhoService.deletarCompra(compra);

        return new ResponseEntity<Compra>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/carrinho/realizar_compra", method = RequestMethod.DELETE)
    public ResponseEntity<?> realizarCompra() {

        List<Compra> compras = carrinhoService.listarCompras();

        if (compras.isEmpty())
            return ErroCarrinho.erroCarrinhoVazio();

        String detalhes = "Detalhes da Compra:\n";
        BigDecimal precoTotal = new BigDecimal(0);

        Optional<Produto> produto;

        for (Compra compra : compras) {
            produto = produtoService.getProdutoById(compra.getIdDoProduto());
            if (produto.isPresent()) {

                BigDecimal precoAtual = produto.get().getPreco().multiply(new BigDecimal(compra.getQuantidade()));

                precoTotal = precoTotal.add(precoAtual);

                detalhes += compra.getQuantidade() + " x " + produto.get().getNome() + " R$ " + produto.get().getPreco()
                        + ". R$ " + precoAtual + "\n";

                // Remover produtos do estoque
                produto.get().removerEstoque(compra.getQuantidade());
                if (produto.get().getEstoque() == 0)
                    produto.get().tornaIndisponivel();

                produtoService.salvarProdutoCadastrado(produto.get());
                carrinhoService.deletarCompra(compra);
            }

        }

        detalhes += "Preço Final: R$" + precoTotal + "\n";

        return new ResponseEntity<String>(detalhes, HttpStatus.OK);
    }

    @RequestMapping(value = "/carrinho", method = RequestMethod.GET)
    public ResponseEntity<?> consultarCarrinho() {

        List<Compra> compras = carrinhoService.listarCompras();

        if (compras.isEmpty()) {
            return ErroCarrinho.erroCarrinhoVazio();
        }

        return new ResponseEntity<List<Compra>>(compras, HttpStatus.OK);
    }
}
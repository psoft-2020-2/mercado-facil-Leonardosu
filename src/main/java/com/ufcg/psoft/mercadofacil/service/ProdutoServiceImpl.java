package com.ufcg.psoft.mercadofacil.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufcg.psoft.mercadofacil.DTO.ProdutoDTO;
import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.ProdutoRepository;

@Service
public class ProdutoServiceImpl implements ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;

	public Optional<Produto> getProdutoById(long id) {
		return produtoRepository.findById(id);
	}

	public List<Produto> getProdutoByCodigoBarra(String codigo) {
		return produtoRepository.findByCodigoBarra(codigo);
	}

	public void removerProdutoCadastrado(Produto produto) {
		produtoRepository.delete(produto);
	}

	public void salvarProdutoCadastrado(Produto produto) {
		produtoRepository.save(produto);
	}

	public List<Produto> listarProdutos() {
		return produtoRepository.findAll();
	}

	public Produto criaProduto(ProdutoDTO produtoDTO) {
		Produto produto = new Produto(produtoDTO.getNome(), produtoDTO.getFabricante(), produtoDTO.getCodigoBarra(),
				produtoDTO.getPreco(), produtoDTO.getCategoria(), produtoDTO.getDescricao());

		produto.tornaDisponivel();
		return produto;
	}

	public Produto atualizaProduto(ProdutoDTO produtoDTO, Produto produto) {

		if (produtoDTO.getNome() != null)
			produto.setNome(produtoDTO.getNome());

		if (produtoDTO.getPreco() != null)
			produto.setPreco(produtoDTO.getPreco());

		if (produtoDTO.getCodigoBarra() != null)
			produto.setCodigoBarra(produtoDTO.getCodigoBarra());

		if (produtoDTO.getFabricante() != null)
			produto.mudaFabricante(produtoDTO.getFabricante());

		if (produtoDTO.getCategoria() != null)
			produto.mudaCategoria(produtoDTO.getCategoria());

		if (produtoDTO.getDescricao() != null)
			produto.mudaDescricao(produtoDTO.getDescricao());

		if (produtoDTO.getFragil() != produto.isFragil())
			produto.setFragil(produtoDTO.getFragil());

		if (produtoDTO.getRefrigeracao() != produto.isRefrigeracao())
			produto.setRefrigeracao(produtoDTO.getRefrigeracao());

		return produto;
	}
}

package com.ufcg.psoft.mercadofacil.DTO;

import java.math.BigDecimal;

public class ProdutoDTO {

	private String nome;

	private BigDecimal preco;

	private String codigoBarra;

	private String fabricante;
	
	private String categoria;
	
	private String descricao;
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public String getCodigoBarra() {
		return codigoBarra;
	}

	public void setCodigoBarra(String codigoBarra) {
		this.codigoBarra = codigoBarra;
	}

	public String getFabricante() {
		return fabricante;
	}

	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	
	public String getDescricao() {
		return this.descricao;
	}
	
	public void setDescricao(String novaDescricao) {
		this.descricao = novaDescricao;
	}
	
}
